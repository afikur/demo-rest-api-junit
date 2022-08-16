package io.github.afikur.demorestapi.controller;

import io.github.afikur.demorestapi.model.Book;
import io.github.afikur.demorestapi.repository.BookRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.print.attribute.standard.Media;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
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
    public void testAddCourse() throws Exception {
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
}
