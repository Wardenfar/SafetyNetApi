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

/**
 * The model for the /childalert route
 */
@NoArgsConstructor
@Getter
@Setter
public class ChildAlertModel {

    @JsonView(Views.ChildAlertModel.class)
    private List<ChildModel> children;

    /**
     * Sub model for each child
     */
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ChildModel {
        @JsonView(Views.ChildAlertModel.class)
        private Person child;
        @JsonView(Views.ChildAlertModel.class)
        private Set<Person> family;
    }

    /**
     * Build the model from a list of children and the personRepository
     */
    public static ChildAlertModel build(Set<Person> children, PersonRepository personRepo) {
        ChildAlertModel model = new ChildAlertModel();

        List<ChildModel> childModels = new ArrayList<>();
        // for each child : build a sub model
        for (Person child : children) {
            ChildModel childModel = new ChildModel();
            childModel.setChild(child);

            // find his family by lastName
            Set<Person> family = personRepo.findAllByLastName(child.getLastName());
            // remove itself
            family.remove(child);

            childModel.setFamily(family);
            childModels.add(childModel);
        }
        model.setChildren(childModels);

        return model;
    }
}
