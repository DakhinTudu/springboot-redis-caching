package com.redisdemo.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data; // actual data list

    private PaginationMeta pagination;  // pagination meta

    private int status;

    private Instant timestamp;
}
