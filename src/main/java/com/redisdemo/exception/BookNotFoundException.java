package com.redisdemo.exception;

import java.util.UUID;

public class BookNotFoundException extends RuntimeException{
    private final UUID bookId; // for logging
    public BookNotFoundException(UUID bookId){
        super("Book Not Found");
        this.bookId=bookId;
    }

    public  UUID getBookId(){  // for logging
        return bookId;
    }
}
