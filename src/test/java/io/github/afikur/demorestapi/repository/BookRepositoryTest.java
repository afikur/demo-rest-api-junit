package io.github.afikur.demorestapi.repository;

import io.github.afikur.demorestapi.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {"/book-schema.sql", "/book-data.sql"})
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Container
    private static MySQLContainer container = new MySQLContainer("mysql:8");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

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
