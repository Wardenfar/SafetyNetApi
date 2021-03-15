package com.safetynet.api.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.MedicalRecord;
import com.safetynet.api.entity.Person;
import com.safetynet.api.model.RestResultModel;
import com.safetynet.api.model.delete.DeleteMedicalRecordModel;
import com.safetynet.api.model.rest.RestMedicalRecordModel;
import com.safetynet.api.repository.MedicalRecordRepository;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.Views;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Rest controller for routes related to the Person Entity
 */
@RestController
public class MedicalRecordController {

    PersonRepository personRepo;
    MedicalRecordRepository medicalRecordRepo;

    /**
     * Constructor
     */
    public MedicalRecordController(PersonRepository personRepo, MedicalRecordRepository medicalRecordRepo) {
        this.personRepo = personRepo;
        this.medicalRecordRepo = medicalRecordRepo;
    }


    /**
     * Create a medicalRecord and link it with a fireStation
     * <p>
     * POST /medicalRecord
     * Json body
     * <p>
     * All parameters are required @see RestMedicalRecordModel
     */
    @PostMapping("/medicalrecord")
    @JsonView(Views.RestResultModel.class)
    RestResultModel addMedicalRecord(@RequestBody RestMedicalRecordModel model) {
        // check required parameters (firstName)
        if (model.getFirstName() == null) {
            return RestResultModel.buildError("POST", "medicalRecord", "FirstName should not be null");
        }
        // check required parameters (lastName)
        if (model.getLastName() == null) {
            return RestResultModel.buildError("POST", "medicalRecord", "LastName should not be null");
        }

        // Search if the medicalRecord already exists
        MedicalRecord existing = this.medicalRecordRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());

        // if no medicalRecord already exists and the fireStation
        if (existing == null) {
            // Search the Person
            Person person = this.personRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
            if (person != null) {
                try {
                    // build the medicalRecord from the model
                    MedicalRecord toAdd = MedicalRecord.fromModel(model);
                    toAdd.setPerson(person);

                    // add it to the repository
                    boolean success = this.medicalRecordRepo.add(toAdd);
                    if (success) {
                        return RestResultModel.buildSuccess("POST", "medicalRecord");
                    } else {
                        return RestResultModel.buildError("POST", "medicalRecord", "Error when adding");
                    }
                } catch (Exception e) {
                    // Catch NullPointerExceptions (if a property is null)
                    return RestResultModel.buildError("POST", "medicalRecord", e.getMessage());
                }
            } else {
                // if the medicalRecord already exists
                return RestResultModel.buildError("POST", "medicalRecord", "The person was not found");
            }
        } else {
            // if the fireStation don't exists
            return RestResultModel.buildError("POST", "medicalRecord", "The combination of firstName and lastName should be unique");
        }
    }

    /**
     * Update a medicalRecord by it's firstName and lastName
     * <p>
     * PUT /medicalRecord
     * Json body
     * <p>
     * Two required parameters : firstName, lastName
     * All others are the same as for POST but they are optional
     */
    @PutMapping("/medicalrecord")
    @JsonView(Views.RestResultModel.class)
    RestResultModel deleteMedicalRecord(@RequestBody RestMedicalRecordModel model) {
        // Check required parameters (firstName)
        if (model.getFirstName() == null) {
            return RestResultModel.buildError("PUT", "medicalRecord", "FirstName should not be null");
        }
        // Check required parameters (lastName)
        if (model.getLastName() == null) {
            return RestResultModel.buildError("PUT", "medicalRecord", "LastName should not be null");
        }

        // if no change throw an error
        if (model.isEmpty()) {
            return RestResultModel.buildError("PUT", "medicalRecord", "Fail : no change");
        }

        // Retrieve the existing medicalRecord by it's name
        MedicalRecord existing = this.medicalRecordRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());

        // Check that the medicalRecord already exist
        if (existing != null) {

            try {
                // Build MedicalRecord entity from Model
                MedicalRecord toUpdate = MedicalRecord.fromModelAndDefault(model, existing);

                // Set it the fireStation
                toUpdate.setPerson(existing.getPerson());

                boolean success = medicalRecordRepo.update(toUpdate);

                if (success) {
                    return RestResultModel.buildSuccess("PUT", "medicalRecord");
                } else {
                    return RestResultModel.buildError("PUT", "medicalRecord", "Error when updating");
                }
            } catch (Exception e) {
                // Throw an error
                return RestResultModel.buildError("PUT", "medicalRecord", e.getMessage());
            }
        } else {
            // else Throw an error
            return RestResultModel.buildError("PUT", "medicalRecord", "The combination of firstName and lastName was not found");
        }
    }

    /**
     * Update a medicalRecord by it's firstName and lastName
     * <p>
     * PUT /medicalRecord
     * Json body
     * <p>
     * Two required parameters : firstName, lastName
     * All others are the same as for POST but they are optional
     */
    @DeleteMapping("/medicalrecord")
    @JsonView(Views.RestResultModel.class)
    RestResultModel deleteMedicalRecord(@RequestBody DeleteMedicalRecordModel model) {
        // Check required parameters (firstName)
        if (model.getFirstName() == null) {
            return RestResultModel.buildError("DELETE", "medicalRecord", "FirstName should not be null");
        }
        // Check required parameters (lastName)
        if (model.getLastName() == null) {
            return RestResultModel.buildError("DELETE", "medicalRecord", "LastName should not be null");
        }

        // fetch the medicalRecord
        MedicalRecord existing = medicalRecordRepo.findOneByFirstNameAndLastName(model.getFirstName(), model.getLastName());
        if (existing != null) {
            // if the medicalRecord was found
            boolean success = medicalRecordRepo.remove(existing);
            if (success) {
                return RestResultModel.buildSuccess("DELETE", "medicalRecord");
            } else {
                return RestResultModel.buildError("DELETE", "medicalRecord", "Error when removing");
            }
        } else {
            // the medicalRecord was not found : error
            return RestResultModel.buildError("DELETE", "medicalRecord", "The medicalRecord does not exists");
        }
    }
}
