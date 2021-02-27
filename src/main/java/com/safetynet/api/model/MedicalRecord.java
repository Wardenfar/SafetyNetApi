package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.util.JsonUtils;
import com.safetynet.api.util.Views;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Getter
@Setter
@EqualsAndHashCode
public class MedicalRecord {

    @NonNull
    @JsonView(Views.Public.class)
    private String birthdate;

    @NonNull
    @JsonView(Views.Public.class)
    private List<String> medications;

    @NonNull
    @JsonView(Views.Public.class)
    private List<String> allergies;

    @JsonView(Views.MedicalRecord.class)
    @EqualsAndHashCode.Exclude
    private Person person;

    public static MedicalRecord fromJson(JsonNode json) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate(json.get("birthdate").asText());
        medicalRecord.setMedications(JsonUtils.readStringList(json.get("medications")));
        medicalRecord.setAllergies(JsonUtils.readStringList(json.get("allergies")));
        return medicalRecord;
    }
}
