package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.util.Views;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * The model for the /firestation route
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class FireStationModel {

    @JsonView(Views.FireStationModel.class)
    private Set<Person> persons;

    @JsonView(Views.FireStationModel.class)
    private long childCount;

    @JsonView(Views.FireStationModel.class)
    private long adultCount;

    @JsonView(Views.FireStationModel.class)
    private long unknownCount;

    /**
     * Build the model from a FireStation
     */
    public static FireStationModel build(FireStation fireStation) {
        // find all persons linked to this FireStation
        Set<Person> persons = fireStation.getPersons();

        FireStationModel model = new FireStationModel();
        model.setPersons(persons);
        // count the number of adults
        model.setAdultCount(persons.stream().filter(p -> p.getMedicalRecord() != null && p.getMedicalRecord().isAdult()).count());
        // count the number of childs
        model.setChildCount(persons.stream().filter(p -> p.getMedicalRecord() != null && !p.getMedicalRecord().isAdult()).count());
        // count the number of person that don't have a medical record
        model.setUnknownCount(persons.stream().filter(p -> p.getMedicalRecord() == null).count());

        return model;
    }
}
