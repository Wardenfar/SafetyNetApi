package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.entity.Person;
import com.safetynet.api.repository.PersonRepository;
import com.safetynet.api.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class ChildAlertModel {

    @JsonView(Views.ChildAlertModel.class)
    private List<ChildModel> children;

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ChildModel {
        @JsonView(Views.ChildAlertModel.class)
        private Person child;
        @JsonView(Views.ChildAlertModel.class)
        private Set<Person> family;
    }

    public static ChildAlertModel build(Set<Person> children, PersonRepository personRepo) {
        ChildAlertModel model = new ChildAlertModel();

        List<ChildModel> childModels = new ArrayList<>();
        for (Person child : children) {
            ChildModel childModel = new ChildModel();
            childModel.setChild(child);
            childModel.setFamily(personRepo.findAllByLastName(child.getLastName()));
            childModels.add(childModel);
        }
        model.setChildren(childModels);

        return model;
    }
}
