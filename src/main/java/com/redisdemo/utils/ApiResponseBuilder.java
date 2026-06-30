package com.redisdemo.utils;

import com.redisdemo.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class ApiResponseBuilder {

    public <T> ResponseEntity<ApiResponse<T>> success(
            T data,
            String message,
            HttpStatus status) {

        ApiResponse<T> response =
                ApiResponse.<T>builder()
                        .success(true)
                        .message(message)
                        .data(data)
                        .status(status.value())
                        .timestamp(Instant.now())
                        .build();

        return ResponseEntity.status(status)
                .body(response);
    }

    public <T> ResponseEntity<ApiResponse<T>> error(
            String message,
            HttpStatus status) {

        ApiResponse<T> response =
                ApiResponse.<T>builder()
                        .success(false)
                        .message(message)
                        .status(status.value())
                        .timestamp(Instant.now())
                        .build();

        return ResponseEntity.status(status)
                .body(response);
    }


    // error method overload

    public <T> ResponseEntity<ApiResponse<T>> error(
            T data,
            String message,
            HttpStatus status) {

        ApiResponse<T> response =
                ApiResponse.<T>builder()
                        .success(false)
                        .message(message)
                        .data(data)
                        .status(status.value())
                        .timestamp(Instant.now())
                        .build();

        return ResponseEntity.status(status)
                .body(response);
    }

}
