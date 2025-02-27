package com.caching.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimezoneModule {
    private String name;
    @JsonProperty("offset_sec")
    private int offsetSec;
    @JsonProperty("offset_string")
    private String offsetString;
}
