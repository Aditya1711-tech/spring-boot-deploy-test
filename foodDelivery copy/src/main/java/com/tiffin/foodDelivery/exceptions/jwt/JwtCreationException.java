package com.tiffin.foodDelivery.exceptions.jwt;

import com.tiffin.foodDelivery.exceptions.common.BaseException;

public class JwtCreationException extends BaseException {
    public JwtCreationException(String message) {
        super(message);
    }

    public JwtCreationException(String message, String errorCode) {
        super(message, errorCode);
    }

    public JwtCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtCreationException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
