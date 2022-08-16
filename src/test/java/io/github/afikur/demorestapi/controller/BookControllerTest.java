package io.github.afikur.demorestapi.controller;

import io.github.afikur.demorestapi.exception.ResourceNotFoundException;
import io.github.afikur.demorestapi.model.Book;
import io.github.afikur.demorestapi.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BookController.class)
public class BookControllerTest {
    @MockBean
    private BookRepository bookRepository;

    @InjectMocks
    private BookController bookController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllBooks() throws Exception {
        List<Book> bookList = List.of(
                new Book(1L, "Junit in action", "Book on unit testing framework", 5),
                new Book(2L, "Spring Microservices in action", "Book on microservice in spring cloud", 5)
        );

        when(bookRepository.findAll()).thenReturn(bookList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/books")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Junit in action")))
                .andExpect(jsonPath("$[1].name", is("Spring Microservices in action")))
                .andExpect(jsonPath("$[1].rating", is(5)));
    }

    @Test
    public void testAddBook() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        String bookJson = "{\"name\":\"Junit in action\",\"summary\":\"Book on unit testing framework\",\"rating\":5}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", Matchers.anything()))
                .andExpect(jsonPath("$.name", is("Junit in action")));
    }

    @Test
    public void testGetBoookById() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/books/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Junit in action")));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        Book updatedBook = new Book(1L, "Junit in action updated", "Book on unit testing framework", 5);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        String bookJson = "{\"name\":\"Junit in action updated\",\"summary\":\"Book on unit testing framework\",\"rating\":5}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Junit in action updated")));
    }

    @Test
    public void testUpdateBook_whenBookIdNotFound() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        Book updatedBook = new Book(1L, "Junit in action updated", "Book on unit testing framework", 5);

        when(bookRepository.findById(anyLong())).thenThrow(ResourceNotFoundException.class);

        String bookJson = "{\"name\":\"Junit in action updated\",\"summary\":\"Book on unit testing framework\",\"rating\":5}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/books/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookJson);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        verify(bookRepository, never()).save(book);
    }

    @Test
    public void testDeleteBookById() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/books/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc
                .perform(requestBuilder)
                .andExpect(status().isOk());

        verify(bookRepository).delete(book);
    }
}
