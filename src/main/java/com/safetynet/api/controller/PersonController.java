package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.exception.EntityNotFound;
import com.safetynet.api.model.ChildAlertModel;
import com.safetynet.api.model.CommunityEmailModel;
import com.safetynet.api.model.RestResultModel;
import com.safetynet.api.model.delete.DeletePersonModel;
import com.safetynet.api.model.rest.RestPersonModel;
import com.safetynet.api.repository.FireStationRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.Views;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Rest controller for routes related to the Person Entity
 */
@RestController
public class PersonController {

    PersonRepository personRepo;
    FireStationRepository fireStationRepo;

    /**
     * Constructor
     */
    public PersonController(PersonRepository personRepo, FireStationRepository fireStationRepo) {
        this.personRepo = personRepo;
        this.fireStationRepo = fireStationRepo;
    }

    /**
     * GET /personInfo?firstName=?&lastName=?
     * <p>
     * Return information about this person
     */
    @GetMapping("/personInfo")
    @JsonView(Views.PersonInfoModel.class)
    Person personInfo(@RequestParam String firstName, @RequestParam String lastName) throws EntityNotFound {
        // find the person by it's firstName and lastName
        Person person = this.personRepo.findOneByFirstNameAndLastName(firstName, lastName);
        if (person == null) { // if the person is null, throw an error
            throw new EntityNotFound("Person not found by firstName and lastName : " + firstName + " " + lastName);
        }
        // else return the person
        return person;
    }

    /**
     * GET /childAlert?address=?
     * <p>
     * Return all children of this address
     */
    @GetMapping("/childAlert")
    @JsonView(Views.ChildAlertModel.class)
    ChildAlertModel childAlert(@RequestParam String address) {
        // find all children from this address
        Set<Person> children = this.personRepo.findAllByAddress(address);
        // build and return the model
        return ChildAlertModel.build(children, this.personRepo);
    }

    /**
     * GET /communityEmail?city=?
     * <p>
     * Return all person's email from a given city
     */
    @GetMapping("/communityEmail")
    CommunityEmailModel communityEmail(@RequestParam String city) {
        // find all persons in the city
        Set<Person> persons = this.personRepo.findAllByCity(city);
        // build and return the model
        return CommunityEmailModel.build(persons);
    }

    /**
     * Create a person and link it with a fireStation
     * <p>
     * POST /person
     * Json body
     * <p>
     * All parameters are required @see RestPersonModel
     */
    @PostMapping("/person")
    @JsonView(Views.RestResultModel.class)
    RestResultModel addPerson(@RequestBody RestPersonModel model) {
        // check required parameters (firstName)
        if (model.getFirstName() == null) {
            return RestResultModel.buildError("POST", "person", "FirstName should not be null");
        }
        // check required parameters (lastName)
        if (model.getLastName() == null) {
            return RestResultModel.buildError("POST", "person", "LastName should not be null");
        }

        // Search if the person already exists
        Person existing = this.personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
        // Search the fireStation
        FireStation fireStation = this.fireStationRepo.findOneByStation(model.getFireStation());

        // if no person already exists and the fireStation
        if (existing == null && fireStation != null) {
            try {
                // build the person from the model
                Person toAdd = Person.fromModel(model);
                toAdd.setFireStation(fireStation);

                // add it to the repository
                this.personRepo.add(toAdd);

                // success
                return RestResultModel.buildSuccess("POST", "person");
            } catch (Exception e) {
                // Catch NullPointerExceptions (if a property is null)
                return RestResultModel.buildError("POST", "person", e.getMessage());
            }
        } else if (existing != null) {
            // if the person already exists
            return RestResultModel.buildError("POST", "person", "The combination of firstName and lastName should be unique");
        } else {
            // if the fireStation don't exists
            return RestResultModel.buildError("POST", "person", "The fireStation was not found");
        }
    }

    /**
     * Update a person by it's firstName and lastName
     * <p>
     * PUT /person
     * Json body
     * <p>
     * Two required parameters : firstName, lastName
     * All others are the same as for POST but they are optional
     */
    @PutMapping("/person")
    @JsonView(Views.RestResultModel.class)
    RestResultModel updatePerson(@RequestBody RestPersonModel model) {
        // Check required parameters (firstName)
        if (model.getFirstName() == null) {
            return RestResultModel.buildError("PUT", "person", "FirstName should not be null");
        }
        // Check required parameters (lastName)
        if (model.getLastName() == null) {
            return RestResultModel.buildError("PUT", "person", "LastName should not be null");
        }

        // Retrieve the existing person by it's name
        Person existing = this.personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());

        // Check that the person already exist
        if (existing != null) {
            // Get the update fireStation or the old one
            FireStation fireStation = model.getFireStation() != null ?
                    this.fireStationRepo.findOneByStation(model.getFireStation())
                    : existing.getFireStation();
            // Check if the fireStation exist
            if (fireStation != null) {
                try {
                    // Build Person entity from Model
                    Person toUpdate = Person.fromModelAndDefault(model, existing);

                    // Set it the fireStation (also for the equal check)
                    toUpdate.setFireStation(fireStation);

                    // Check if they're is some changes
                    if (existing.equals(toUpdate)) {
                        // if no change throw an error
                        return RestResultModel.buildError("PUT", "person", "Fail : no change");
                    }

                    personRepo.update(toUpdate);

                    // success
                    return RestResultModel.buildSuccess("PUT", "person");
                } catch (Exception e) {
                    // Throw an error
                    return RestResultModel.buildError("PUT", "person", e.getMessage());
                }
            } else {
                // else Throw an error
                return RestResultModel.buildError("PUT", "person", "The fireStation was not found");
            }
        } else {
            // else Throw an error
            return RestResultModel.buildError("PUT", "person", "The combination of firstName and lastName was not found");
        }
    }

    /**
     * Update a person by it's firstName and lastName
     * <p>
     * PUT /person
     * Json body
     * <p>
     * Two required parameters : firstName, lastName
     * All others are the same as for POST but they are optional
     */
    @DeleteMapping("/person")
    @JsonView(Views.RestResultModel.class)
    RestResultModel updatePerson(@RequestBody DeletePersonModel model) {
        // Check required parameters (firstName)
        if (model.getFirstName() == null) {
            return RestResultModel.buildError("DELETE", "person", "FirstName should not be null");
        }
        // Check required parameters (lastName)
        if (model.getLastName() == null) {
            return RestResultModel.buildError("DELETE", "person", "LastName should not be null");
        }

        // fetch the person
        Person existing = personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
        if (existing != null) {
            // if the person was found
            personRepo.remove(existing);
            return RestResultModel.buildSuccess("DELETE", "person");
        } else {
            // the person was not found : error
            return RestResultModel.buildError("DELETE", "person", "The person does not exists");
        }
    }
}
