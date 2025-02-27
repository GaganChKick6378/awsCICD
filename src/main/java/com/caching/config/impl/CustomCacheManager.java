package com.caching.config.impl;

import com.caching.caching.CustomLRUCache;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom CacheManager implementation that manages caches with specific settings like size and TTL.
 */
public class CustomCacheManager implements CacheManager {

    private final Map<String, CustomLRUCache> caches;
    private static final long CACHE_TTL_IN_MILLIS = 120000; // 2 minutes in milliseconds
    private static final int CACHE_MAX_SIZE = 5;

    public CustomCacheManager() {
        caches = new HashMap<>();
        caches.put("geocoding", new CustomLRUCache("geocoding", CACHE_MAX_SIZE, CACHE_TTL_IN_MILLIS));
        caches.put("reverse-geocoding", new CustomLRUCache("reverse-geocoding", CACHE_MAX_SIZE, CACHE_TTL_IN_MILLIS));
    }

    /**
     * Retrieves a cache by name.
     *
     * @param name The name of the cache.
     * @return The cache instance corresponding to the name.
     */
    @Override
    public Cache getCache(String name) {
        return caches.get(name);
    }

    /**
     * Retrieves all cache names.
     *
     * @return A collection of cache names.
     */
    @Override
    public Collection<String> getCacheNames() {
        return caches.keySet();
    }
}
