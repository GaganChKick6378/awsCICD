package com.caching.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatumTwo {
    private double latitude;
    private double longitude;
    private String type;
    private double distance;
    private String name;
    private String number;
    @JsonProperty("postal_code")
    private String postalCode;
    private String street;
    private double confidence;
    private String region;
    @JsonProperty("region_code")
    private String regionCode;
    private String county;
    private String locality;
    @JsonProperty("administrative_area")
    private Object administrativeArea;
    private String neighbourhood;
    private String country;
    @JsonProperty("country_code")
    private String countryCode;
    private String continent;
    private String label;
}
