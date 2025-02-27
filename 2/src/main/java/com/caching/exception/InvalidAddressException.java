package com.caching.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAddressException extends GeocodingException {
    public InvalidAddressException(String address) {
        super("Invalid address: " + address);
    }
}
