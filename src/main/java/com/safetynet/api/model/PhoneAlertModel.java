package com.safetynet.api.model;

import com.safetynet.api.entity.FireStation;
import com.safetynet.api.entity.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The model for the /phonealert route
 */
@NoArgsConstructor
@Getter
@Setter
public class PhoneAlertModel {

    private Set<String> phones;

    /**
     * Build the model from a list of fireStations
     */
    public static PhoneAlertModel build(List<FireStation> fireStations){
        PhoneAlertModel model = new PhoneAlertModel();
        Set<Person> persons = fireStations.stream()
                .flatMap(f -> f.getPersons().stream())
                .collect(Collectors.toSet());
        // find all phones of the set
        model.setPhones(persons.stream().map(Person::getPhone).collect(Collectors.toSet()));
        return model;
    }
}
