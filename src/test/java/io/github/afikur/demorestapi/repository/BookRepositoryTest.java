package io.github.afikur.demorestapi.repository;

import io.github.afikur.demorestapi.model.Book;
import io.github.afikur.demorestapi.testcontainer.MySQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class BookRepositoryTest implements MySQLContainerInitializer {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @Commit
    public void getBook_returnsBookDetails() {
        Book book = new Book("The Alchemist", "The Alchemist is a novel by Brazilian author Paulo Coelho " +
                "which was first published in 1988. Originally written in Portuguese", 5);

        bookRepository.save(book);

        Book savedBook = bookRepository
                .findById(book.getBookId())
                .orElse(null);

        assertAll(() -> {
            assertNotNull(savedBook);
            assertEquals(book.getName(), savedBook.getName());
            assertEquals(book.getRating(), savedBook.getRating());
        });
    }


    @Test
    public void getBook_returnsNullForInvalidId() {
        Book book = bookRepository
                .findById(1000L)
                .orElse(null);

        assertNull(book);
    }
}
