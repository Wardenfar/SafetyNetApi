package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.util.DateUtils;
import com.safetynet.api.util.JsonUtils;
import com.safetynet.api.util.Views;
import lombok.*;

import java.util.List;

/**
 * the MedicalRecord Entity
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MedicalRecord {

    @NonNull
    @JsonView(Views.MedicalRecordProperties.class)
    private String birthdate;

    @NonNull
    @JsonView(Views.MedicalRecordProperties.class)
    private List<String> medications;

    @NonNull
    @JsonView(Views.MedicalRecordProperties.class)
    private List<String> allergies;

    @JsonView(Views.MedicalRecordRelations.class)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Person person;

    /**
     * Calculate the age from the birthday data
     */
    public int age() {
        return DateUtils.ageInYears(DateUtils.parseDate(birthdate));
    }

    /**
     * Return true if age() >= 18
     */
    public boolean isAdult() {
        return DateUtils.ageInYears(DateUtils.parseDate(birthdate)) >= 18;
    }

    /**
     * Build an entity from the json extracted from the data source
     *
     * @param json json node
     * @return the medicalRecord
     */
    public static MedicalRecord fromJson(JsonNode json) {
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setBirthdate(json.get("birthdate").asText());
        medicalRecord.setMedications(JsonUtils.readStringList(json.get("medications")));
        medicalRecord.setAllergies(JsonUtils.readStringList(json.get("allergies")));
        return medicalRecord;
    }
}
