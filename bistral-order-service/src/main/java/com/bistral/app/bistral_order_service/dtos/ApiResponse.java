package com.bistral.app.bistral_order_service.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApiResponse<T> {
    String message;
    Boolean isError = false;
    T data;
    Map<String, Object> meta = new HashMap<>();


    public static <T> ApiResponse<T> ok(T response, String message) {
        return ApiResponse.<T>builder()
                .data(response)
                .message(message)
                .isError(false)
                .build();
    }

    public static ApiResponse<Void> error(String message) {
        return ApiResponse.<Void>builder()
                .isError(true)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> ok(T response, String message, Map<String, Object> meta) {
        return ApiResponse.<T>builder()
                .data(response)
                .message(message)
                .isError(false)
                .meta(meta)
                .build();
    }

    public static ApiResponse<Void> error(String message, Map<String, Object> meta) {
        return ApiResponse.<Void>builder()
                .isError(true)
                .message(message)
                .meta(meta)
                .build();
    }


}
