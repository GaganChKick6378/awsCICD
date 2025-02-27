package com.caching.mapper;

import com.caching.constant.Constants;
import com.caching.dto.out.Coordinate;
import com.caching.model.CoordinateResponse;
import com.caching.model.DatumOne;
import com.caching.model.DatumTwo;
import com.caching.model.LocationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GlobalMapper {
    /**
     * Converts a LocationResponse to a Coordinate object.
     */
    public Coordinate convertToCoordinate(LocationResponse locationResponse) {
        if (locationResponse != null && locationResponse.getData() != null && !locationResponse.getData().isEmpty()) {
            DatumOne datum = locationResponse.getData().get(0);
            log.info("The latitude and longitude are : {}{}", datum.getLatitude(), " , "+datum.getLongitude());
            return new Coordinate(datum.getLatitude(), datum.getLongitude());
        }
        throw new IllegalArgumentException(Constants.INVALID_LOCATION);
    }
    /**
     * Converts a CoordinateResponse to an address string.
     */
    public String convertToAddress(CoordinateResponse coordinateResponse) {
        if (coordinateResponse != null && coordinateResponse.getData() != null && !coordinateResponse.getData().isEmpty()) {
            DatumTwo datum = coordinateResponse.getData().get(0);
            return datum.getLabel();
        }
        throw new IllegalArgumentException(Constants.INVALID_COORDINATE);
    }
}
