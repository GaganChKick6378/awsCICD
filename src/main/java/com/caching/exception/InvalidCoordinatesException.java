package com.caching.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCoordinatesException extends GeocodingException {
    public InvalidCoordinatesException(double latitude, double longitude) {
        super("Invalid coordinates: Latitude " + latitude + ", Longitude " + longitude);
    }
}