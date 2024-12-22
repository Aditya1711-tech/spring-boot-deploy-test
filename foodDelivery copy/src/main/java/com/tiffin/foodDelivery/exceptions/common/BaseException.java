package com.tiffin.foodDelivery.exceptions.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BaseException extends RuntimeException {
    private final String errorCode;

    public BaseException(String message) {
        super(message);
        this.errorCode = "GENERAL_ERROR"; // Default error code
    }

    public BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "GENERAL_ERROR"; // Default error code
    }

    public BaseException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}

