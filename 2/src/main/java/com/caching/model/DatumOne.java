package com.caching.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatumOne {
    private double latitude;
    private double longitude;
    private String type;
    private String name;
    private String number;
    @JsonProperty("postal_code")
    private String postalCode;
    private String street;
    private int confidence;
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
    @JsonProperty("timezone_module")
    private TimezoneModule timezoneModule;
}
