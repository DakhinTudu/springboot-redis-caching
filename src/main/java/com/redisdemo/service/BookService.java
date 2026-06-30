package com.redisdemo.service;

import com.redisdemo.dto.BookRequest;
import com.redisdemo.dto.BookResponse;
import com.redisdemo.dto.BookUpdateRequest;

import java.util.List;
import java.util.UUID;
public interface BookService {

    public BookResponse create(BookRequest book);

    public BookResponse update(UUID bookId, BookUpdateRequest book);

    public BookResponse getBookById(UUID bookId);

    public List<BookResponse> getBooks(int page, int size);

    public void delete(UUID bookId);
}

