package com.caching.constant;

public class Constants {
    private Constants() {
        throw new UnsupportedOperationException("This is just a constants class.");
    }

    public static final String REMOVED_FROM_CACHE = "Removed least recently used entry with key: {} from cache '{}'";
    public static final String ENTRY_EXPIRED = "Entry is expired. Current time: {}, Last access time: {}, TTL: {} ms";
    public static final String CLEANING= "Cleaned up {} expired entries from cache '{}'";
    public static final String REMOVING_ENTRY="Removing expired entry for key: {} from cache '{}'";
    public static final String CACHE_MISS="Cache miss or expired for key: {} in cache '{}'";
    public static final String CACHE_HIT="Cache hit for the key: {} in cache '{}'";
    public static final String MISS_EXPIRED ="Cache miss or expired for the key: {} in cache '{}'";
    public static final String ERROR_LOADING = "Error loading value for key: {} in cache '{}'";
    public static final String STORED_KEY= "Stored key: {} in cache '{}'. Current cache size: {}";
    public static final String EVICTED_KEY = "Evicted key: {} from cache '{}'";
    public static final String CLEARING_ENTRY="Cleared all entries from cache '{}'";
    public static final String INITIALISATION="Initialized CustomLRUCache '{}' with max size {} and TTL {} ms";
    public static final String INVALID_LOCATION= "Invalid LocationResponse: No data available";
    public static final String INVALID_COORDINATE = "Invalid CoordinateResponse: No data available";
    public static final String ADDRESS_DISPLAY="The corresponding address is {}";
    public static final String API_CALL_FAILED = "Reverse geocoding API call failed for coordinates: {}, {}";
    public static final String REVERSE_API_CALL = "Calling reverse geocoding API for coordinates: {}, {}";
    public static final String FORWARD_API_CALL = "Calling forward geocoding API for address: {}";
    public static final String GEOCODING_API_FAILED = "Geocoding API call failed for address: {}";
}
