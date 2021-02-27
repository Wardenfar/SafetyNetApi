package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.Views;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class PersonController {

    PersonRepository personRepo;

    public PersonController(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/personInfo")
    @JsonView(Views.PublicAndPerson.class)
    Set<Person> personInfo(@RequestParam String firstName, @RequestParam String lastName) {
        return this.personRepo.findAllByFirstNameAndLastName(firstName, lastName);
    }
}
