package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.model.rest.RestPersonModel;
import com.safetynet.api.util.Views;
import lombok.*;

/**
 * The Person entity
 */
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"firstName", "lastName"})
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString()
public class Person {

    @NonNull
    @JsonView({Views.PersonFirstName.class})
    @ToString.Include
    private String firstName;

    @NonNull
    @JsonView({Views.PersonLastName.class})
    @ToString.Include
    private String lastName;

    @NonNull
    @JsonView({Views.PersonAddress.class})
    @ToString.Include
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

    /**
     * Dynamic Json property
     *
     * @return the age from the medicalRecord
     */
    @JsonView({Views.PersonAge.class})
    @JsonProperty(value = "age", access = JsonProperty.Access.READ_ONLY)
    public String ageJson() {
        if (medicalRecord != null) {
            return String.valueOf(medicalRecord.age());
        } else {
            return "Unknown";
        }
    }

    @Override
    public Person clone(){
        Person result = new Person();
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setCity(city);
        result.setZip(zip);
        result.setAddress(address);
        result.setEmail(email);
        result.setPhone(phone);
        result.setFireStation(fireStation);
        result.setMedicalRecord(medicalRecord);
        return result;
    }

    /**
     * Build an entity from the json extracted from the data source
     *
     * @param json json node
     * @return the person
     */
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

    /**
     * Build an entity from RestPersonModel
     */
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

    /**
     * Build an entity from RestPersonModel with default
     */
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
