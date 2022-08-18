package io.github.afikur.demorestapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue
    private Long bookId;

    @NotNull
    private String name;

    @NotNull
    private String summary;

    private int rating;

    public Book() {
    }

    public Book(Long bookId, String name, String summary, int rating) {
        this.bookId = bookId;
        this.name = name;
        this.summary = summary;
        this.rating = rating;
    }

    public Book(String name, String summary, int rating) {
        this.name = name;
        this.summary = summary;
        this.rating = rating;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", rating=" + rating +
                '}';
    }
}
