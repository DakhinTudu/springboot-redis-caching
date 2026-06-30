package com.redisdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookRequest(
        @NotBlank(message = "Book Name is required")
        @Size(max = 50, min = 3, message = "Book Name must be between 3 to 50 characters")
        String bookName,
        @NotBlank(message = "Author Name is required")
        @Size(max = 50, min = 3, message = "Author Name must be between 3 to 50 characters")
        String authorName
) {
}
