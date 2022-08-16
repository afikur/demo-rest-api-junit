package io.github.afikur.demorestapi.controller;

import io.github.afikur.demorestapi.model.Book;
import io.github.afikur.demorestapi.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/books")
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping(value = "/{bookId}")
    public Book getBookById(@PathVariable("bookId") Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book id not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@RequestBody @Valid Book book) {
        return bookRepository.save(book);
    }

    @PutMapping(value = "/{bookId}")
    public Book updateBook(@PathVariable("bookId") Long bookId, @RequestBody @Valid Book book) {
        Book bookRecord = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book id not found"));

        bookRecord.setName(book.getName());
        bookRecord.setSummary(book.getSummary());
        bookRecord.setRating(book.getRating());

        return bookRepository.save(bookRecord);
    }
}
