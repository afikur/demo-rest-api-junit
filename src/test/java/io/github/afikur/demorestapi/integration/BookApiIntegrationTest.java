package io.github.afikur.demorestapi.integration;

import io.github.afikur.demorestapi.model.Book;
import io.github.afikur.demorestapi.testcontainer.MySQLContainerInitializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BookApiIntegrationTest implements MySQLContainerInitializer {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"/book-data.sql"})
    public void getBook_returnsBookDetails() {
        ResponseEntity<Book> response = restTemplate
                .getForEntity("/books/1", Book.class);

        assert response.getBody() != null;

        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getBody().getName(), "Junit in action");
            assertEquals(response.getBody().getRating(), 5);
        });
    }

    @Test
    public void createBook_returnsNewBook() {
        Book book = new Book("Junit in action", "Book on unit testing framework", 5);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Book> entity = new HttpEntity<>(book, headers);

        ResponseEntity<Book> response = restTemplate
                .postForEntity("/books", entity, Book.class);

        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.CREATED);
            assertEquals(response.getBody().getName(), "Junit in action");
            assertEquals(response.getBody().getRating(), 5);
        });
    }

    @Test
    @Sql(scripts = {"/book-data.sql"})
    public void updateBook_ShouldUpdateTheBookDetails() {
        Book book = new Book("Junit in action updated", "Book on unit testing framework", 4);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<Book> entity = new HttpEntity<>(book, headers);

        ResponseEntity<Book> response = restTemplate
                .exchange("/books/1", HttpMethod.PUT, entity, Book.class);

        assertAll(() -> {
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getBody().getName(), "Junit in action updated");
            assertEquals(response.getBody().getRating(), 4);
        });
    }

    @Test
    @Sql(scripts = {"/book-data.sql"})
    public void deleteBook_ShouldDeleteBook() {
        restTemplate.delete("/books/2");

        ResponseEntity<Book> response = restTemplate
                .getForEntity("/books/2", Book.class);

        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}
