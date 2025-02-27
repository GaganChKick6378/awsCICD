package com.caching.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class GeocodingApiException extends GeocodingException {
    public GeocodingApiException(String message) {
        super("Geocoding API error: " + message);
    }
}