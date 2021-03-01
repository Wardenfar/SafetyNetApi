package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.model.post.PostPersonModel;
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

    public static Person fromModel(PostPersonModel model) {
        Person p = new Person();
        p.setFirstName(model.getFirstName());
        p.setLastName(model.getLastName());
        p.setAddress(model.getAddress());
        p.setCity(model.getCity());
        p.setZip(model.getZip());
        p.setPhone(model.getPhone());
        p.setEmail(model.getEmail());
//        p.setFirstName(Objects.requireNonNull(model.getFirstName()));
//        p.setLastName(Objects.requireNonNull(model.getLastName()));
//        p.setAddress(Objects.requireNonNull(model.getAddress()));
//        p.setCity(Objects.requireNonNull(model.getCity()));
//        p.setZip(Objects.requireNonNull(model.getZip()));
//        p.setPhone(Objects.requireNonNull(model.getPhone()));
//        p.setEmail(Objects.requireNonNull(model.getEmail()));
        return p;
    }
}
