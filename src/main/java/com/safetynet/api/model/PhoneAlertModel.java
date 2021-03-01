package com.safetynet.api.model;

import com.safetynet.api.entity.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
     * Build the model from a set of Person
     * @param persons
     * @return
     */
    public static PhoneAlertModel build(Set<Person> persons){
        PhoneAlertModel model = new PhoneAlertModel();
        // find all phones of the set
        model.setPhones(persons.stream().map(Person::getPhone).collect(Collectors.toSet()));
        return model;
    }
}
