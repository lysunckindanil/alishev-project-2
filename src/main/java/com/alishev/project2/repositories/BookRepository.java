package com.alishev.project2.repositories;

import com.alishev.project2.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByNameStartsWith(String name);
}
