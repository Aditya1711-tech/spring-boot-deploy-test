package com.tiffin.foodDelivery.exceptions;

import com.tiffin.foodDelivery.dtos.common.ResponseDTO;
import com.tiffin.foodDelivery.exceptions.client.LoginFailedException;
import com.tiffin.foodDelivery.exceptions.common.BaseException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginFailedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseDTO<String>> handleEmailAlreadyExistsException(LoginFailedException ex) {
        return buildResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO<String>> handleBadRequestException(BadRequestException ex) {
        return buildResponse(ex.getMessage(), "400", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ResponseDTO<String>> handleBaseException(BaseException ex) {
        return buildResponse(ex.getMessage(), ex.getErrorCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<String>> handleException(Exception ex) {
        return buildResponse(ex.getMessage(), "GENERAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseDTO<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        return new ResponseEntity<>(ResponseDTO.error(errorMessage, "VALIDATION_ERROR"), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ResponseDTO<String>> buildResponse(String message, String errorCode, HttpStatus status) {
        ResponseDTO<String> response = ResponseDTO.error(message, errorCode);
        return new ResponseEntity<>(response, status);
    }
}
