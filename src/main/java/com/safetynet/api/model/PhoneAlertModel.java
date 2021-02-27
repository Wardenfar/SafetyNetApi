package com.safetynet.api.model;

import com.safetynet.api.entity.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class PhoneAlertModel {

    private Set<String> phones;

    public static PhoneAlertModel build(Set<Person> persons){
        PhoneAlertModel model = new PhoneAlertModel();
        model.setPhones(persons.stream().map(Person::getPhone).collect(Collectors.toSet()));
        return model;
    }
}
