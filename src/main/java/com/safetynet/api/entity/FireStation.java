package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.util.Views;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * The fireStation Entity
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"station"})
@ToString
public class FireStation {

    @NonNull
    @JsonView(Views.FireStationProperties.class)
    private String station;

    @JsonView(Views.FireStationProperties.class)
    private String address;

    /**
     * Set of persons linked to this fireStation
     */
    @JsonView(Views.FireStationRelations.class)
    @ToString.Exclude
    private Set<Person> persons = new HashSet<>();

    /**
     * Build an entity from the json extracted from the data source
     *
     * @param json json node
     * @return the fireStation
     */
    public static FireStation fromJson(JsonNode json) {
        FireStation fireStation = new FireStation();
        fireStation.setStation(json.get("station").asText());
        fireStation.setAddress(json.get("address").asText());
        return fireStation;
    }

    /**
     * Link a person to this fireStation
     */
    public void addPerson(Person person) {
        persons.add(person);
    }

    /**
     * Remove the link between this and the person
     */
    public void removePerson(Person person) {
        persons.remove(person);
    }
}
