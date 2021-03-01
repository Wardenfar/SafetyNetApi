package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.model.rest.RestPersonModel;
import com.safetynet.api.util.Views;
import lombok.*;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Person {

    @NonNull
    @JsonView({Views.PersonFirstName.class})
    private String firstName;

    @NonNull
    @JsonView({Views.PersonLastName.class})
    private String lastName;

    @NonNull
    @JsonView({Views.PersonAddress.class})
    private String address;

    @NonNull
    @JsonView({Views.PersonAddress.class})
    private String city;

    @NonNull
    @JsonView({Views.PersonAddress.class})
    private String zip;

    @NonNull
    @JsonView({Views.PersonPhone.class})
    private String phone;

    @NonNull
    @JsonView({Views.PersonEmail.class})
    private String email;

    @NonNull
    @JsonView({Views.PersonFireStation.class})
    private FireStation fireStation;

    @JsonView({Views.PersonMedicalRecord.class})
    private MedicalRecord medicalRecord;

    @JsonView({Views.PersonAge.class})
    @JsonProperty(value = "age", access = JsonProperty.Access.READ_ONLY)
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

    public static Person fromModel(RestPersonModel model) {
        Person p = new Person();
        p.setFirstName(model.getFirstName());
        p.setLastName(model.getLastName());
        p.setAddress(model.getAddress());
        p.setCity(model.getCity());
        p.setZip(model.getZip());
        p.setPhone(model.getPhone());
        p.setEmail(model.getEmail());
        return p;
    }

    public static Person fromModelAndDefault(RestPersonModel model, Person defaultObject) {
        Person p = new Person();
        p.setFirstName(model.getFirstName() != null ? model.getFirstName() : defaultObject.getFirstName());
        p.setLastName(model.getLastName() != null ? model.getLastName() : defaultObject.getLastName());
        p.setAddress(model.getAddress() != null ? model.getAddress() : defaultObject.getAddress());
        p.setCity(model.getCity() != null ? model.getCity() : defaultObject.getCity());
        p.setZip(model.getZip() != null ? model.getZip() : defaultObject.getZip());
        p.setPhone(model.getPhone() != null ? model.getPhone() : defaultObject.getPhone());
        p.setEmail(model.getEmail() != null ? model.getEmail() : defaultObject.getEmail());
        return p;
    }
}
