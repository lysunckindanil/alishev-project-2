package com.alishev.project2.controllers;

import com.alishev.project2.models.Book;
import com.alishev.project2.models.Person;
import com.alishev.project2.services.BookService;
import com.alishev.project2.services.PersonService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @GetMapping()
    public String index(Model model, @RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "books_per_page", required = false) Integer books_per_page, @RequestParam(name = "sort_by_year", required = false) Boolean sorted) {
        if (page != null && books_per_page != null && sorted != null && sorted) {
            model.addAttribute("books", bookService.findAll(PageRequest.of(page, books_per_page, Sort.by("year"))));
        } else if (page != null && books_per_page != null) {
            model.addAttribute("books", bookService.findAll(PageRequest.of(page, books_per_page)));
        } else if (sorted != null && sorted) {
            model.addAttribute("books", bookService.findAll(Sort.by("year")));
        } else {
            model.addAttribute("books", bookService.findAll());
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String book(@PathVariable int id, Model model) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            if (book.get().getOwner() != null) {
                model.addAttribute("person", book.get().getOwner());
            } else {
                model.addAttribute("person", new Person());
                model.addAttribute("people", personService.findAll());
            }
            return "books/profile";
        }
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBookGet(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping("/new")
    public String newBookPost(@ModelAttribute @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBookGet(@PathVariable int id, Model model) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "books/edit";
        }
        return "redirect:/books";
    }

    @PostMapping("/{id}/edit")
    public String editBookPost(@PathVariable int id, @ModelAttribute @Valid Book book, BindingResult bindingResult) {
        book.setBook_id(id);
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.update(book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable int id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/free")
    public String freeBook(@PathVariable int id) {
        bookService.freeBookById(id);
        return "redirect:/books/" + id;
    }

    @PostMapping("/{book_id}/set")
    public String setPersonBook(@PathVariable int book_id, @ModelAttribute Person person) {
        bookService.updateBookPerson(book_id, person);
        return "redirect:/books/" + book_id;
    }

    @GetMapping("/search")
    public String searchBook() {
        return "books/search";
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    @ResponseBody
    @PostMapping("/search")
    public List<BookResponse> getBooksStartWithString(@RequestParam(name = "string") String string) {
        List<Book> books = bookService.getBooksStartWithString(string);
        List<BookResponse> booksResponse = new ArrayList<>();
        for (Book book : books) {
            BookResponse bookResponse = new BookResponse(
                    book.getName(), book.getBook_id(), book.getAuthor(), book.getOwner() == null ? -1 : book.getOwner().getPerson_id());
            booksResponse.add(bookResponse);
        }
        return booksResponse;
    }

    @AllArgsConstructor
    @Data
    static class BookResponse {
        private String book_name;
        private int book_id;
        private String author;
        private int person_id;
    }

}


