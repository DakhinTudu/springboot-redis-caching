package com.redisdemo.dto;

import java.util.UUID;

public record BookResponse(
        UUID bookId,
        String bookName,
        String authorName
) {
}
