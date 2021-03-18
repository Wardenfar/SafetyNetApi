package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.util.Views;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * Build the model from a list of FireStations
     */
    public static FireStationModel build(List<FireStation> fireStations) {
        // find all persons linked to this FireStations
        Set<Person> persons = fireStations
                .stream()
                .flatMap(f -> f.getPersons().stream())
                .collect(Collectors.toSet());

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
