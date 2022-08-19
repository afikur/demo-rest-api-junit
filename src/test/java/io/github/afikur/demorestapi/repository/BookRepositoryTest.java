package io.github.afikur.demorestapi.repository;

import io.github.afikur.demorestapi.model.Book;
import io.github.afikur.demorestapi.testcontainer.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/book-schema.sql", "/book-data.sql"})
public class BookRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void getBook_returnsBookDetails() {
        Book book = bookRepository
                .findById(1L)
                .orElse(null);

        assertAll(() -> {
            assertNotNull(book);
            assertEquals("Junit in action", book.getName());
            assertEquals(5, book.getRating());
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
