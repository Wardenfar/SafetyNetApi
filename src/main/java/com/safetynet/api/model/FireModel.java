package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

/**
 * The model for the /fire route
 */
@NoArgsConstructor
@Getter
@Setter
public class FireModel {

    @JsonView(Views.FireModel.class)
    FireStation fireStation;

    @JsonView(Views.FireModel.class)
    Set<Person> persons;

    /**
     * Build the model from a FireStation
     */
    public static FireModel build(FireStation fireStation){
        FireModel model = new FireModel();
        model.setFireStation(fireStation);
        model.setPersons(fireStation.getPersons());
        return model;
    }
}
