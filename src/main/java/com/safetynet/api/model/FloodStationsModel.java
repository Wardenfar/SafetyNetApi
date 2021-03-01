package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class FloodStationsModel {

    @JsonView(Views.FloodStationsModel.class)
    private List<FireStationSubModel> fireStations;

    public static FloodStationsModel build(Set<FireStation> fireStations) {
        FloodStationsModel model = new FloodStationsModel();

        List<FireStationSubModel> subModels = new ArrayList<>();
        for (FireStation f : fireStations) {
            FireStationSubModel subModel = new FireStationSubModel();
            subModel.setFireStation(f);
            subModel.setPersonsByAddress(f.getPersons().stream().collect(Collectors.groupingBy(Person::getAddress)));
            subModels.add(subModel);
        }

        model.setFireStations(subModels);
        return model;
    }

    @NoArgsConstructor
    @Setter
    @Getter
    static class FireStationSubModel {

        @JsonView(Views.FloodStationsModel.class)
        private FireStation fireStation;

        @JsonView(Views.FloodStationsModel.class)
        private Map<String, List<Person>> personsByAddress;
    }
}
