package com.tiffin.foodDelivery.exceptions.client;

import com.tiffin.foodDelivery.exceptions.common.BaseException;

public class RecordNotFoundException extends BaseException {
    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, String errorCode) {
        super(message, errorCode);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecordNotFoundException(String message, Throwable cause, String errorCode) {
        super(message, cause, errorCode);
    }
}
