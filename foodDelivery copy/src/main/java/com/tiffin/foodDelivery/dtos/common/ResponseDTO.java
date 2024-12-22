package com.tiffin.foodDelivery.dtos.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {

    private String status;
    private String message;
    private T data;

    public ResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public static <T> ResponseDTO<T> success(String message, T data) {
        return new ResponseDTO<>("success", message, data);
    }

    public static <T> ResponseDTO<T> success(String message) {
        return new ResponseDTO<>("success", message, null);
    }

    public static <T> ResponseDTO<T> error(String message, String errorCode) {
        return new ResponseDTO<>("error", message, null);
    }

    public static <T> ResponseDTO<T> error(String errorCode, T data) {
        return new ResponseDTO<>("error", errorCode, data);
    }

    public static <T> ResponseDTO<T> of(String status, String message, T data) {
        return new ResponseDTO<>(status, message, data);
    }
}
