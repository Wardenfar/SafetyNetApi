package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.exception.EntityNotFound;
import com.safetynet.api.model.ChildAlertModel;
import com.safetynet.api.model.CommunityEmailModel;
import com.safetynet.api.model.post.PostPersonModel;
import com.safetynet.api.model.post.PostResultModel;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.Views;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class PersonController {


    PersonRepository personRepo;
    FireStationRepository fireStationRepo;

    public PersonController(PersonRepository personRepo, FireStationRepository fireStationRepo) {
        this.personRepo = personRepo;
        this.fireStationRepo = fireStationRepo;
    }

    @GetMapping("/personInfo")
    @JsonView(Views.PersonInfoModel.class)
    Person personInfo(@RequestParam String firstName, @RequestParam String lastName) throws EntityNotFound {
        Person person = this.personRepo.findOneByFirstNameAndLastName(firstName, lastName);
        if (person == null) {
            throw new EntityNotFound("Person not found by firstName and lastName : " + firstName + " " + lastName);
        }
        return person;
    }

    @GetMapping("/childAlert")
    @JsonView(Views.ChildAlertModel.class)
    ChildAlertModel childAlert(@RequestParam String address) {
        Set<Person> children = this.personRepo.findAllByAddress(address);
        return ChildAlertModel.build(children, this.personRepo);
    }

    @GetMapping("/communityEmail")
    CommunityEmailModel communityEmail(@RequestParam String city) {
        Set<Person> persons = this.personRepo.findAllByCity(city);
        return CommunityEmailModel.build(persons);
    }

    @PostMapping("/person")
    @JsonView(Views.PostResultModel.class)
    PostResultModel addPerson(@RequestBody PostPersonModel model) {
        if(model.getFirstName() == null){
            return PostResultModel.buildError("person", "FirstName should not be null");
        }
        if(model.getLastName() == null){
            return PostResultModel.buildError("person", "LastName should not be null");
        }

        Person existing = this.personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
        FireStation fireStation = this.fireStationRepo.findOneByStation(model.getFireStation());
        if (existing == null && fireStation != null) {
            try {
                Person toAdd = Person.fromModel(model);
                toAdd.setFireStation(fireStation);
                this.personRepo.add(toAdd);
                return PostResultModel.buildSuccess("person", personRepo.count());
            } catch (Exception e) {
                return PostResultModel.buildError("person", e.getMessage());
            }
        } else if (existing != null) {
            return PostResultModel.buildError("person", "The combination of firstName and lastName should be unique");
        } else {
            return PostResultModel.buildError("person", "The fireStation was not found");
        }
    }
}
