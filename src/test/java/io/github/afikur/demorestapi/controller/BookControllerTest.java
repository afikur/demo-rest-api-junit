package io.github.afikur.demorestapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = BookController.class)
public class BookControllerTest {
    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void findAllBooks_ShouldReturnAllBooks() throws Exception {
        List<Book> bookList = List.of(
                new Book(1L, "Junit in action", "Book on unit testing framework", 5),
                new Book(2L, "Spring Microservices in action", "Book on microservice in spring cloud", 5)
        );

        given(bookRepository.findAll()).willReturn(bookList);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Junit in action")))
                .andExpect(jsonPath("$[1].name", is("Spring Microservices in action")))
                .andExpect(jsonPath("$[1].rating", is(5)));
    }

    @Test
    public void addBook_ShouldCreateANewBook() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        given(bookRepository.save(any(Book.class))).willReturn(book);

        Book bookToSave = new Book("Junit in action", "Book on unit testing framework", 5);
        String bookJson = objectMapper.writeValueAsString(bookToSave);

        mockMvc
                .perform(post("/books")
                        .content(bookJson)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", is(1)))
                .andExpect(jsonPath("$.name", is(book.getName())));
    }

    @Test
    public void getBookById_ShouldReturnBookDetails() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));

        mockMvc
                .perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Junit in action")));
    }

    @Test
    public void updateBook_ShouldUpdateABookDetails() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        Book updatedBook = new Book(1L, "Junit in action updated", "Book on unit testing framework", 5);

        given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));
        given(bookRepository.save(any(Book.class))).willReturn(updatedBook);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc
                .perform(put("/books/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Junit in action updated")));
    }

    @Test
    public void updateBookWithWrongId_ShouldReturnNotFound() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);
        Book updatedBook = new Book(1L, "Junit in action updated", "Book on unit testing framework", 5);

        given(bookRepository.findById(anyLong())).willThrow(ResourceNotFoundException.class);

        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc
                .perform(put("/books/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        then(bookRepository)
                .should(never())
                .save(book);
    }

    @Test
    public void deleteBook_ShouldDeleteABook() throws Exception {
        Book book = new Book(1L, "Junit in action", "Book on unit testing framework", 5);

        given(bookRepository.findById(anyLong())).willReturn(Optional.of(book));

        mockMvc
                .perform(delete("/books/1"))
                .andExpect(status().isOk());

        then(bookRepository)
                .should()
                .delete(book);
    }

    @Test
    public void deleteBookWithInvalidId_ShouldThrowResourceNotFoundException() throws Exception {
        Book book = new Book(1000L, "Junit in action", "Book on unit testing framework", 5);

        given(bookRepository.findById(anyLong())).willThrow(ResourceNotFoundException.class);

        mockMvc
                .perform(delete("/books/1000"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException));

        then(bookRepository)
                .should(never())
                .delete(book);
    }
}
