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
public class CommunityEmailModel {

    Set<String> emails;

    public static CommunityEmailModel build(Set<Person> persons) {
        CommunityEmailModel model = new CommunityEmailModel();
        Set<String> emails = persons.stream().map(Person::getEmail).collect(Collectors.toSet());
        model.setEmails(emails);
        return model;
    }
}
