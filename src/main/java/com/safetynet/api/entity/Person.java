package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.safetynet.api.util.Views;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class Person {

    @NonNull
    @JsonView({Views.Public.class, Views.BasicPerson.class})
    private String firstName;

    @NonNull
    @JsonView({Views.Public.class, Views.BasicPerson.class})
    private String lastName;

    @NonNull
    @JsonView({Views.Public.class, Views.BasicPerson.class})
    private String address;

    @NonNull
    @JsonView(Views.Public.class)
    private String city;

    @NonNull
    @JsonView(Views.Public.class)
    private String zip;

    @NonNull
    @JsonView({Views.Public.class, Views.BasicPerson.class})
    private String phone;

    @NonNull
    @JsonView(Views.Public.class)
    private String email;

    @JsonView(Views.Person.class)
    private FireStation fireStation;

    @JsonView(Views.Person.class)
    private MedicalRecord medicalRecord;

    @JsonView({Views.Public.class, Views.BasicPerson.class})
    @JsonProperty("age")
    public String ageJson() {
        if (medicalRecord != null) {
            return String.valueOf(medicalRecord.age());
        } else {
            return "Unknown";
        }
    }

    public static Person fromJson(JsonNode json) {
        Person p = new Person();
        p.setFirstName(json.get("firstName").asText());
        p.setLastName(json.get("lastName").asText());
        p.setAddress(json.get("address").asText());
        p.setCity(json.get("city").asText());
        p.setZip(json.get("zip").asText());
        p.setPhone(json.get("phone").asText());
        p.setEmail(json.get("email").asText());
        return p;
    }
}
