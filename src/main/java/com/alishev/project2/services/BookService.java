package com.alishev.project2.services;

import com.alishev.project2.models.Book;
import com.alishev.project2.models.Person;
import com.alishev.project2.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).getContent();
    }

    public List<Book> findAll(Sort sort) {
        return bookRepository.findAll(sort);
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    public List<Book> getBooksStartWithString(String string) {
        List<Book> books = bookRepository.findBooksByNameStartsWith(string);
        Hibernate.initialize(books);
        return books;
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void freeBookById(int id) {
        Optional<Book> book = findById(id);
        book.ifPresent(value -> {
            value.setOwner(null);
            value.setTerm(null);
        });
    }

    @Transactional
    public void updateBookPerson(int book_id, Person person) {
        Optional<Book> book = bookRepository.findById(book_id);
        book.ifPresent(value -> {
            book.get().setTerm(new Date(new Date().getTime() + 10 * 24 * 60 * 60 * 1000));
            person.addBook(book.get());
        });
    }

}
