package com.tiffin.foodDelivery.exceptions.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tiffin.foodDelivery.exceptions.common.BaseException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginFailedException extends BaseException {

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, String errorCode) {
        super(message, errorCode);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
