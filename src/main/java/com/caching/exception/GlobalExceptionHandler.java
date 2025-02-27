package com.caching.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler that handles exceptions and returns custom error responses for client and API errors.
 */

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * Handles client-side errors (InvalidAddressException, InvalidCoordinatesException) and returns a custom error response.
     */
    @ExceptionHandler({InvalidAddressException.class, InvalidCoordinatesException.class})
    public ResponseEntity<Object> handleClientErrors(GeocodingException ex, WebRequest request) {
        log.error("Client Error: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("details", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles API-side errors (GeocodingApiException) and returns a custom error response.
     */
    @ExceptionHandler(GeocodingApiException.class)
    public ResponseEntity<Object> handleApiErrors(GeocodingApiException ex, WebRequest request) {
        log.error("API Error: {}", ex.getMessage());

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("details", request.getDescription(false));

        return new ResponseEntity<>(body, HttpStatus.SERVICE_UNAVAILABLE);
    }
}