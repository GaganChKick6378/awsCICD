package com.caching.dto.out;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO class representing geographical coordinates with latitude and longitude which will
 *  be returned in response.
 */

@Getter
@Setter
@AllArgsConstructor
public class Coordinate {
    private double latitude;
    private double longitude;
}
