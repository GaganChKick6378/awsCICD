package com.caching.service;

import com.caching.constant.Constants;
import com.caching.dto.out.Coordinate;
import com.caching.exception.InvalidAddressException;
import com.caching.exception.InvalidCoordinatesException;
import com.caching.exception.GeocodingApiException;
import com.caching.mapper.GlobalMapper;
import com.caching.model.CoordinateResponse;
import com.caching.model.LocationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Service class that provides methods for forward and reverse geocoding using external APIs.
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationService {

    private final RestTemplate restTemplate;
    private final GlobalMapper globalMapper;
    private static final int MIN_ADDRESS_LENGTH = 3;

    @Value("${geocoding-url}")
    private String forwardApi;

    @Value("${reverse-geocoding-url}")
    private String reverseApi;

    /**
     * Retrieves the geographic coordinates (latitude and longitude) for a given address.
     */

    @Cacheable(value = "geocoding", key = "#address", unless = "#result == null || #address.equalsIgnoreCase('goa')")
    public Coordinate getCoordinates(String address) {
        if (!StringUtils.hasText(address) || address.trim().length() < MIN_ADDRESS_LENGTH || "invalid_address".equalsIgnoreCase(address)) {
            throw new InvalidAddressException("Address must have at least " + MIN_ADDRESS_LENGTH + " characters");
        }

        try {
            String finalAPI = buildForwardGeocodingAPI(address);
            log.info(Constants.FORWARD_API_CALL, address);

            ResponseEntity<LocationResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, LocationResponse.class);

            if (response.getBody() == null) {
                throw new NullPointerException("Response body is null for coordinates: ");
            }

            if (response.getStatusCode() != HttpStatus.OK ) {
                throw new GeocodingApiException("Invalid geocoding response");
            }

            return globalMapper.convertToCoordinate(response.getBody());
        } catch (RestClientException e) {
            log.error(Constants.GEOCODING_API_FAILED, address, e);
            throw new GeocodingApiException("Geocoding API call failed");
        } catch (NullPointerException e) {
            log.error("NullPointerException encountered: ", e);
            throw e;
        }
    }


    /**
     * Retrieves the address for a given latitude and longitude.
     */

    @Cacheable(value = "reverse-geocoding", key = "{#latitude, #longitude}", unless = "#result == null")
    public String getAddress(double latitude, double longitude) {
        validateCoordinates(latitude, longitude);

        try {
            String finalAPI = buildReverseGeocodingAPI(latitude, longitude);
            log.info(Constants.REVERSE_API_CALL, latitude, longitude);

            ResponseEntity<CoordinateResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, CoordinateResponse.class);


            if (response.getBody() == null) {
                throw new NullPointerException("Response body is null for coordinates: " + latitude + ", " + longitude);
            }

            if (response.getStatusCode() != HttpStatus.OK ) {
                throw new GeocodingApiException("Invalid reverse geocoding response");
            }

            String address = globalMapper.convertToAddress(response.getBody());
            log.info(Constants.ADDRESS_DISPLAY, address);
            return address;
        } catch (RestClientException e) {
            log.error(Constants.API_CALL_FAILED, latitude, longitude, e);
            throw new GeocodingApiException("Reverse geocoding API call failed");
        } catch (NullPointerException e) {
            log.error("NullPointerException encountered: ", e);
            throw e;
        }
    }


    /**
     * Validates that the given latitude and longitude are within valid geographic bounds.
     */

    private void validateCoordinates(double latitude, double longitude) {
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            throw new InvalidCoordinatesException(latitude, longitude);
        }
    }

    /**
     * Constructs the API URL for forward geocoding with the provided address.
     */
    private String buildForwardGeocodingAPI(String address) {
        return forwardApi.replace("ADDRESS", URLEncoder.encode(address, StandardCharsets.UTF_8));
    }

    /**
     * Constructs the API URL for reverse geocoding with the provided latitude and longitude.
     */
    private String buildReverseGeocodingAPI(double latitude, double longitude) {
        return reverseApi.replace("LATITUDE", String.valueOf(latitude)).replace("LONGITUDE", String.valueOf(longitude));
    }
}