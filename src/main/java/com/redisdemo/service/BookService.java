package com.redisdemo.service;

import com.redisdemo.entity.Books;
import com.redisdemo.repository.BookRepository;
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
public class BookService {

    private final BookRepository repository;
    private final PaginationMapper paginationMapper;

    @CacheEvict(value = "page-books", allEntries = true)
    public String create(Books book) {
        repository.save(book);
        return "Book Created Successfully";
    }



    @Caching(evict = {
            @CacheEvict(value = "books", key = "#bookId"),
            @CacheEvict(value = "page-books", allEntries = true)
    })
    @Transactional
    public String update(UUID bookId, Books book) {
        Books existingBook = repository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));

        if (hasText(book.getBookName())) {
            existingBook.setBookName(book.getBookName());
        }

        if (hasText(book.getAuthorName())) {
            existingBook.setAuthorName(book.getAuthorName());
        }

        //   repository.save(existingBook); // no need of this if method is annotated with @Transactional

        return "Book Updated Successfully";
    }



    @Transactional(readOnly = true)
    @Cacheable(value = "books", key = "#bookId")
    public Books getBookById(UUID bookId) {
        return repository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "page-books", key = "#page + '-' + #size")
    public List<Books> getBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Books> booksPage = repository.findAll(pageable);

        return booksPage.stream().toList();
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