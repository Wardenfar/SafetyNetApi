package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.Person;
import com.safetynet.api.exception.EntityNotFound;
import com.safetynet.api.model.ChildAlertModel;
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
    @JsonView(Views.PersonInfoModel.class)
    Person personInfo(@RequestParam String firstName, @RequestParam String lastName) throws EntityNotFound {
        Person person = this.personRepo.findAllByFirstNameAndLastName(firstName, lastName);
        if(person == null) {
            throw new EntityNotFound();
        }
        return person;
    }

    @GetMapping("/childAlert")
    @JsonView(Views.ChildAlertModel.class)
    ChildAlertModel childAlert(@RequestParam String address) {
        Set<Person> children = this.personRepo.findAllByAddress(address);
        return ChildAlertModel.build(children, this.personRepo);
    }
}
