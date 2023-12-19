package com.alishev.project2.controllers;

import com.alishev.project2.models.Person;
import com.alishev.project2.services.PersonService;
import com.alishev.project2.util.PersonValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/people")
public class PersonController {

    private final PersonValidator personValidator;
    private final PersonService personService;

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", personService.findAll());
        return "people/index";
    }

    @GetMapping("/{id}")
    public String person(@PathVariable int id, Model model) {
        Optional<Person> person = personService.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            model.addAttribute("books", person.get().getBooks());
            return "people/profile";
        }
        return "redirect:/people";
    }

    @GetMapping("/new")
    public String newPersonGet(Model model) {
        model.addAttribute("person", new Person());
        return "people/new";
    }

    @PostMapping("/new")
    public String newPersonPost(@ModelAttribute @Valid Person person, BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) return "people/new";
        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String editPersonGet(@PathVariable int id, Model model) {
        Optional<Person> person = personService.findById(id);
        person.ifPresent(value -> model.addAttribute("person", value));
        return "people/edit";
    }

    @PostMapping("/{id}/edit")
    public String editPersonPost(@ModelAttribute @Valid Person person, BindingResult bindingResult, @PathVariable int id) {
        person.setPerson_id(id);
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "people/edit";
        }
        personService.update(person);
        return "redirect:/people";
    }

    @PostMapping("/{id}/delete")
    public String deletePerson(@PathVariable int id) {
        personService.deleteById(id);
        return "redirect:/people";
    }
}
