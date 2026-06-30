package com.redisdemo.controller;

import com.redisdemo.dto.ApiResponse;
import com.redisdemo.dto.BookRequest;
import com.redisdemo.dto.BookResponse;
import com.redisdemo.dto.BookUpdateRequest;
import com.redisdemo.entity.Books;
import com.redisdemo.service.BookService;
import com.redisdemo.utils.ApiResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ApiResponseBuilder apiResponseBuilder;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody BookRequest book) {
        return apiResponseBuilder.success(bookService.create(book),"Book create successfully",HttpStatus.CREATED);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable UUID bookId) {
        return apiResponseBuilder.success(bookService.getBookById(bookId),"Records fetched successfully",HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return apiResponseBuilder
                .success(bookService.getBooks(page, size), "Books fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable UUID bookId,
            @RequestBody BookUpdateRequest book) {
        return apiResponseBuilder.success(bookService.update(bookId, book),"Book updated successfully",HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable UUID bookId) {
        bookService.delete(bookId);
        return apiResponseBuilder.success(null,"Book deleted successfully",HttpStatus.NO_CONTENT);
    }
}