package com.alishev.project2.services;

import com.alishev.project2.models.Person;
import com.alishev.project2.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {
    private final PersonRepository personRepository;

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        Optional<Person> person = personRepository.findById(id);
        person.ifPresent(value -> Hibernate.initialize(value.getBooks()));
        return person;
    }


    public Optional<Person> findByName(String name) {
        return personRepository.findPersonByName(name);
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void deleteById(int id) {
        personRepository.deleteById(id);
    }
}
