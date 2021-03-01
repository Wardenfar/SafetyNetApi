package com.safetynet.api.model;

import com.safetynet.api.entity.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * The model for the /communityEmail route
 */
@NoArgsConstructor
@Getter
@Setter
public class CommunityEmailModel {

    Set<String> emails;

    /**
     * Build the model from a list of persons
     */
    public static CommunityEmailModel build(Set<Person> persons) {
        CommunityEmailModel model = new CommunityEmailModel();
        // find a list of emails from the list of person
        Set<String> emails = persons.stream().map(Person::getEmail).collect(Collectors.toSet());
        model.setEmails(emails);
        return model;
    }
}
