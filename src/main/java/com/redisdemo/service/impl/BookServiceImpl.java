package com.redisdemo.service.impl;


import com.redisdemo.dto.BookRequest;
import com.redisdemo.dto.BookResponse;
import com.redisdemo.dto.BookUpdateRequest;
import com.redisdemo.entity.Books;
import com.redisdemo.mapper.BookMapper;
import com.redisdemo.repository.BookRepository;
import com.redisdemo.service.BookService;
import com.redisdemo.utils.PaginationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final PaginationMapper paginationMapper;
    private final BookMapper bookMapper;

    @CacheEvict(value = "page-books", allEntries = true)
    public BookResponse create(BookRequest book) {
        return bookMapper.toResponse(repository.save(bookMapper.toEntity(book)));
    }


    @Caching(evict = {
            @CacheEvict(value = "books", key = "#bookId"),
            @CacheEvict(value = "page-books", allEntries = true)
    })
    @Transactional
    public BookResponse update(UUID bookId, BookUpdateRequest book) {
        Books existingBook = repository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        if (hasText(book.bookName())) {
            existingBook.setBookName(book.bookName());
        }

        if (hasText(book.authorName())) {
            existingBook.setAuthorName(book.authorName());
        }

        //   repository.save(existingBook); // no need of this if method is annotated with @Transactional

        return bookMapper.toResponse(existingBook);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#bookId")
    public BookResponse getBookById(UUID bookId) {
       Books book = repository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
       return bookMapper.toResponse(book);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "page-books", key = "#page + '-' + #size")
    public List<BookResponse> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> booksPage = repository.findAll(pageable);

        return bookMapper.toResponseList(booksPage.stream().toList());
    }


    @Transactional
    @Caching(evict = {

            @CacheEvict(value = "books", key = "#bookId"),
            @CacheEvict(value = "page-books", allEntries = true)
    })
    public void delete(UUID bookId) {

        Books book = repository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        repository.delete(book);
    }
}

/*

@CachePut is used when the method returns the updated entity
 */