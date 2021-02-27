package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.util.Views;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class FireStation {

    @NonNull
    @JsonView(Views.Public.class)
    private String station;

    @NonNull
    @JsonView(Views.Public.class)
    private String address;

    @JsonView(FireStation.class)
    @EqualsAndHashCode.Exclude
    private Set<Person> persons = new HashSet<>();

    public static FireStation fromJson(JsonNode json) {
        FireStation fireStation = new FireStation();
        fireStation.setStation(json.get("station").asText());
        fireStation.setAddress(json.get("address").asText());
        return fireStation;
    }

    public void add(Person person) {
        persons.add(person);
    }
}
