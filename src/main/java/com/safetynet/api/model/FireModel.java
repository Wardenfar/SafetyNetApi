package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import com.safetynet.api.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class FireModel {

    @JsonView(Views.PublicAndMedicalRecord.class)
    FireStation fireStation;

    @JsonView(Views.PublicAndMedicalRecord.class)
    Set<Person> persons;

    public static FireModel build(FireStation fireStation){
        FireModel model = new FireModel();
        model.setFireStation(fireStation);
        model.setPersons(fireStation.getPersons());
        return model;
    }
}
