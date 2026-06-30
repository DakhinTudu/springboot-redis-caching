package com.redisdemo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;

    private String message;

    private T data; // actual data list

    private PaginationMeta pagination;  // pagination meta

    private int status;

    private Instant timestamp;
}
