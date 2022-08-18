package io.github.afikur.demorestapi.repository;

import io.github.afikur.demorestapi.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getBook_returnsBookDetails() {
        Book savedBook = entityManager.persistFlushFind(new Book("Junit in action", "Book on unit testing", 5));

        Book book = bookRepository
                .findById(1L)
                .orElse(null);

        assert book != null;

        assertAll(() -> {
            assertNotNull(book);
            assertEquals(book.getName(), savedBook.getName());
            assertEquals(book.getSummary(), savedBook.getSummary());
            assertEquals(book.getRating(), savedBook.getRating());
        });
    }
}
