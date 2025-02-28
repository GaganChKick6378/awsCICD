package com.caching.controller;

import com.caching.dto.out.Coordinate;
import com.caching.exception.GeocodingApiException;
import com.caching.exception.InvalidAddressException;
import com.caching.exception.InvalidCoordinatesException;
import com.caching.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller that handles geocoding and reverse geocoding requests,
 * and manages the exceptions thrown during the geocoding process.
 */
@RestController
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    /**
     * Endpoint to perform forward geocoding and retrieve coordinates for a given address.
     *
     * @param address The address to geocode.
     * @return A response entity containing the coordinates or an error message.
     */
    @GetMapping("/api/geocoding")
    public ResponseEntity<Coordinate> forwardGeocoding(@RequestParam String address) {
        try {
            Coordinate coordinate = locationService.getCoordinates(address);
            return new ResponseEntity<>(coordinate, HttpStatus.OK);
        } catch (InvalidAddressException e) {
            throw new InvalidAddressException(address);
        } catch (GeocodingApiException e) {
            throw new GeocodingApiException("Geocoding API error: " + e.getMessage());
        }
    }

    /**
     * Endpoint to perform reverse geocoding and retrieve the address for a given latitude and longitude.
     *
     * @param latitude  The latitude to reverse geocode.
     * @param longitude The longitude to reverse geocode.
     * @return A response entity containing the address or an error message.
     */
    @GetMapping("/reverse-geocoding")
    public ResponseEntity<String> reverseGeocoding(@RequestParam double latitude, @RequestParam double longitude) {
        try {
            String address = locationService.getAddress(latitude, longitude);
            return ResponseEntity.ok(address);
        } catch (InvalidCoordinatesException e) {
            throw new InvalidCoordinatesException(latitude, longitude);
        } catch (GeocodingApiException e) {
            throw new GeocodingApiException("Geocoding API error: " + e.getMessage());
        }
    }

    /**
     * Exception handler for invalid address errors.
     *
     * @param e The exception thrown when an invalid address is provided.
     * @return A response entity with the error message and a BAD_REQUEST status.
     */

    @ExceptionHandler(InvalidAddressException.class)
    public ResponseEntity<String> handleInvalidAddressException(InvalidAddressException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for invalid coordinates errors.
     *
     * @param e The exception thrown when invalid coordinates are provided.
     * @return A response entity with the error message and a BAD_REQUEST status.
     */

    @ExceptionHandler(InvalidCoordinatesException.class)
    public ResponseEntity<String> handleInvalidCoordinatesException(InvalidCoordinatesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Exception handler for geocoding API errors.
     *
     * @param e The exception thrown during geocoding API failures.
     * @return A response entity with the error message and an INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(GeocodingApiException.class)
    public ResponseEntity<String> handleGeocodingApiException(GeocodingApiException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
