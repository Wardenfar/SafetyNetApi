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

/**
 * The model for the /flood/stations route
 */
@NoArgsConstructor
@Getter
@Setter
public class FloodStationsModel {

    @JsonView(Views.FloodStationsModel.class)
    private List<FireStationSubModel> fireStations;

    /**
     * Build the model from a list of fireStations
     *
     * @param fireStations list of FireStations
     * @return the model
     */
    public static FloodStationsModel build(Set<FireStation> fireStations) {
        FloodStationsModel model = new FloodStationsModel();

        List<FireStationSubModel> subModels = new ArrayList<>();
        // for each FireStation : build a sub model
        for (FireStation f : fireStations) {
            FireStationSubModel subModel = new FireStationSubModel();
            subModel.setFireStation(f);
            // groups persons by address
            subModel.setPersonsByAddress(f.getPersons().stream().collect(Collectors.groupingBy(Person::getAddress)));
            // and add it to the list
            subModels.add(subModel);
        }

        model.setFireStations(subModels);
        return model;
    }

    /**
     * Sub model for each FireStation
     */
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
