package com.redisdemo.config;

import com.redisdemo.entity.Books;
import com.redisdemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {

        if(bookRepository.count()==0) {
            Books book1 = new Books(null,"The Hobbit", "J.R.R. Tolkien");
            Books book2 = new Books(null,"1984", "George Orwell");
            Books book3 = new Books(null, "To Kill a Mockingbird", "Harper Lee");
            Books book4 = new Books(null, "The Great Gatsby", "F. Scott Fitzgerald");
            Books book5 = new Books(null, "Pride and Prejudice", "Jane Austen");

            bookRepository.saveAll(List.of(book1, book2, book3, book4, book5));

            System.out.println("Samples books inserted into the database successfully!");
        }
    }
}
