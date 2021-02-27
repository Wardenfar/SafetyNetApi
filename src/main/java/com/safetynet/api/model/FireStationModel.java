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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter
@Getter
public class FireStationModel {

    @JsonView(Views.FireStationModel.class)
    List<Person> persons;

    @JsonView(Views.FireStationModel.class)
    long childCount;

    @JsonView(Views.FireStationModel.class)
    long adultCount;

    public static FireStationModel build(FireStation fireStation) {
        List<Person> persons = fireStation.getPersons();

        FireStationModel model = new FireStationModel();
        model.setPersons(persons);
        model.setAdultCount(persons.stream().filter(p -> p.getMedicalRecord().isAdult()).count());
        model.setChildCount(persons.stream().filter(p -> !p.getMedicalRecord().isAdult()).count());
        return model;
    }
}
