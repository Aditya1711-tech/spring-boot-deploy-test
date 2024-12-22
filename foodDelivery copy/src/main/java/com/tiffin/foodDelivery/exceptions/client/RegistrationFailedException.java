package com.tiffin.foodDelivery.exceptions.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tiffin.foodDelivery.exceptions.common.BaseException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegistrationFailedException extends BaseException {

    public RegistrationFailedException(String message) {
        super(message);
    }

    public RegistrationFailedException(String message, String errorCode) {
        super(message, errorCode);
    }

    public RegistrationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationFailedException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
