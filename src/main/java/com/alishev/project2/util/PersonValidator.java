package com.alishev.project2.util;

import com.alishev.project2.models.Person;
import com.alishev.project2.services.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@SuppressWarnings("NullableProblems")
@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Person.class.equals(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> person1 = personService.findByName(person.getName());
        if (person1.isPresent() && person1.get().getPerson_id() != person.getPerson_id()) {
            errors.rejectValue("name", "", "Person with this name already exists!");
        }
    }
}
