package com.redisdemo.controller;

import com.redisdemo.dto.ApiResponse;
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
    public ResponseEntity<String> createBook(@RequestBody Books book) {
        return ResponseEntity.ok(bookService.create(book));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<Books> getBookById(@PathVariable UUID bookId) {
        return ResponseEntity.ok(bookService.getBookById(bookId));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<Books>>> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {


        return apiResponseBuilder
                .success(bookService.getBooks(page, size), "Books fetched successfully", HttpStatus.OK);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<String> updateBook(
            @PathVariable UUID bookId,
            @RequestBody Books book) {

        return ResponseEntity.ok(bookService.update(bookId, book));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable UUID bookId) {
        bookService.delete(bookId);
        return ResponseEntity.ok("Book Deleted Successfully");
    }
}