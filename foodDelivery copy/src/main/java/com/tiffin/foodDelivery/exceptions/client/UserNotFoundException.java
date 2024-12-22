package com.tiffin.foodDelivery.exceptions.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tiffin.foodDelivery.exceptions.common.BaseException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserNotFoundException extends BaseException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
