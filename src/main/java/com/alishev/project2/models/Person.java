package com.alishev.project2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private int person_id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty!")
    @Size(min = 5, max = 128, message = "Name should be between 5 and 128 characters!")
    private String name;

    @Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REFRESH)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        book.setOwner(this);
        books.add(book);
    }
}
