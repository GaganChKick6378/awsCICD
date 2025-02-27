package com.caching.config;

import com.caching.config.impl.CustomCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


/**
 * Configuration class for setting up caching in the application.
 * Enables caching and configures custom cache manager and cache settings.
 */
@Configuration
@EnableCaching
public class CachingConfig {
    /**
     * Configures the custom CacheManager bean that manages multiple caches.
     *
     * @return The CacheManager with custom caches for "geocoding" and "reverse-geocoding".
     */

    @Bean
    public CacheManager cacheManager() {
        return new CustomCacheManager();
    }

    /**
     * Configures the RestTemplate bean that will be used for making HTTP requests.
     *
     * @return A new instance of RestTemplate.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
