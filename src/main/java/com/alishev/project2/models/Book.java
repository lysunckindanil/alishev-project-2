package com.alishev.project2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer book_id;

    @Column(name = "book_name")
    @NotEmpty(message = "Book name should not be empty!")
    @Size(min = 2, max = 256, message = "Book name should be between 2 and 256 characters!")
    private String name;

    @Column(name = "author")
    @NotEmpty(message = "Author name should not be empty!")
    @Size(min = 2, max = 256, message = "Author name should be between 2 and 128 characters!")
    private String author;

    @Column(name = "year")
    @Min(value = 0, message = "Year should not me less than 0!")
    @Max(value = 2030, message = "Year should be less than 2030!")
    private Integer year;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "term")
    private Date term;

    public boolean isOutOfTerm() {
        return term != null && term.before(new Date());
    }

}
