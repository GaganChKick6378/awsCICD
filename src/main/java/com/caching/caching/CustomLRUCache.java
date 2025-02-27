package com.caching.caching;

import com.caching.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Custom implementation of an LRU (Least Recently Used) cache with a Time-To-Live (TTL) feature.
 * This cache evicts the least recently used entry when the cache exceeds the maximum size, and it also
 * removes expired entries based on the TTL.
 * <p>
 * This class supports basic cache operations such as `put`, `get`, `evict`, and `clear`,
 * and it also handles automatic cleanup of expired cache entries.
 */
@Slf4j
public class CustomLRUCache implements Cache {
    private final String name;
    private final int maxSize;
    private final long ttlMillis;
    private final Map<Object, CacheEntry> cache;
    private final ScheduledExecutorService scheduler;

    public CustomLRUCache(String name, int maxSize, long ttlMillis) {
        this.name = name;
        this.maxSize = maxSize;
        this.ttlMillis = ttlMillis;
        this.cache = new ConcurrentHashMap<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduler.scheduleAtFixedRate(this::cleanupExpiredEntries, ttlMillis, ttlMillis, TimeUnit.MILLISECONDS);
        log.info(Constants.INITIALISATION, name, maxSize, ttlMillis);
    }

    /**
     * Returns the name of the cache.
     *
     * @return the cache's name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the underlying native cache instance.
     *
     * @return the native cache object
     */
    @Override
    public Object getNativeCache() {
        return cache;
    }

    /**
     * Retrieves the value for a given key from the cache.
     *
     * @param key the key to look up
     * @return a ValueWrapper containing the value associated with the key, or null if not found or expired
     */
    @Override
    public ValueWrapper get(Object key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || isExpired(entry)) {
            log.debug(Constants.CACHE_MISS, key, name);
            return null;
        }
        entry.lastAccessTime = System.currentTimeMillis();
        log.debug(Constants.CACHE_HIT, key, name);
        return () -> entry.value;
    }

    /**
     * Retrieves the value for a given key and returns it as the specified type.
     *
     * @param key  the key to look up
     * @param type the class type to cast the value to
     * @param <T>  the type of the value
     * @return the value of the requested type, or null if not found or expired
     */
    @Override
    public <T> T get(Object key, Class<T> type) {
        CacheEntry entry = cache.get(key);
        if (entry == null || isExpired(entry)) {
            log.debug(Constants.MISS_EXPIRED, key, name);
            return null;
        }
        entry.lastAccessTime = System.currentTimeMillis();
        log.debug("Cache hit for key: {} in cache '{}'", key, name);
        return (T) entry.value;
    }

    /**
     * Retrieves the value for a given key using a value loader function if the key is not found or expired.
     *
     * @param key         the key to look up
     * @param valueLoader a callable that loads the value if it's not in the cache
     * @param <T>         the type of the value
     * @return the value loaded by the valueLoader, or null if an error occurs
     */
    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = (T) get(key, Object.class);
        if (value == null) {
            try {
                value = valueLoader.call();
                put(key, value);
            } catch (Exception e) {
                log.error(Constants.ERROR_LOADING, key, name, e);
                return null;
            }
        }
        return value;
    }

    /**
     * Adds a key-value pair to the cache. If the cache exceeds the maximum size, the least recently used entry is evicted.
     *
     * @param key   the key for the cache entry
     * @param value the value to store in the cache
     */
    @Override
    public void put(Object key, Object value) {
        if (cache.size() >= maxSize) {
            removeLeastRecentlyUsed();
        }

        cache.put(key, new CacheEntry(value, System.currentTimeMillis()));
        log.debug(Constants.STORED_KEY, key, name, cache.size());
    }

    /**
     * Removes the entry for a given key from the cache.
     *
     * @param key the key to remove from the cache
     */
    @Override
    public void evict(Object key) {
        cache.remove(key);
        log.debug(Constants.EVICTED_KEY, key, name);
    }

    /**
     * Clears all entries from the cache.
     */
    @Override
    public void clear() {
        cache.clear();
        log.info(Constants.CLEARING_ENTRY, name);
    }

    /**
     * Periodically cleans up expired entries from the cache based on their TTL.
     * This method is run in a separate thread at a fixed rate.
     */
    private void cleanupExpiredEntries() {
        int initialSize = cache.size();
        cache.entrySet().removeIf(entry -> {
            boolean expired = isExpired(entry.getValue());
            if (expired) {
                log.debug(Constants.REMOVING_ENTRY, entry.getKey(), name);
            }
            return expired;
        });
        int removedCount = initialSize - cache.size();
        if (removedCount > 0) {
            log.info(Constants.CLEANING, removedCount, name);
        }
    }

    /**
     * Checks if a cache entry is expired based on its TTL.
     *
     * @param entry the cache entry to check
     * @return true if the entry is expired, false otherwise
     */
    private boolean isExpired(CacheEntry entry) {
        boolean expired = System.currentTimeMillis() - entry.lastAccessTime > ttlMillis;
        if (expired) {
            log.debug(Constants.ENTRY_EXPIRED, System.currentTimeMillis(), entry.lastAccessTime, ttlMillis);
        }
        return expired;
    }

    private void removeLeastRecentlyUsed() {
        Object lruKey = cache.entrySet().stream().min(Map.Entry.comparingByValue((a, b) -> Long.compare(a.lastAccessTime, b.lastAccessTime))).map(Map.Entry::getKey).orElse(null);

        if (lruKey != null) {
            cache.remove(lruKey);
            log.info(Constants.REMOVED_FROM_CACHE, lruKey, name);
        }
    }

    private static class CacheEntry {
        Object value;
        long lastAccessTime;

        CacheEntry(Object value, long lastAccessTime) {
            this.value = value;
            this.lastAccessTime = lastAccessTime;
        }
    }
}