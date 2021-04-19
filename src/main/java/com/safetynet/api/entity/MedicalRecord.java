package com.safetynet.api.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.JsonNode;
import com.safetynet.api.model.rest.RestMedicalRecordModel;
import com.safetynet.api.util.DateUtils;
import com.safetynet.api.util.JsonUtils;
import com.safetynet.api.util.Views;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * the MedicalRecord Entity
 */
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"person"})
@ToString()
public class MedicalRecord {

    @NonNull
    @JsonView(Views.MedicalRecordProperties.class)
    @ToString.Include
    private String birthdate;

    @NonNull
    @JsonView(Views.MedicalRecordProperties.class)
    @ToString.Include
    private List<String> medications;

    @NonNull
    @JsonView(Views.MedicalRecordProperties.class)
    @ToString.Include
    private List<String> allergies;

    @JsonView(Views.MedicalRecordRelations.class)
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


    @Override
    public MedicalRecord clone(){
        MedicalRecord result = new MedicalRecord();
        result.setBirthdate(birthdate);
        result.setMedications(medications);
        result.setAllergies(allergies);
        result.setPerson(person.clone());
        return result;
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

    /**
     * Build an entity from RestPersonModel
     */
    public static MedicalRecord fromModel(RestMedicalRecordModel model) {
        MedicalRecord mr = new MedicalRecord();
        mr.setBirthdate(model.getBirthdate());
        mr.setMedications(model.getMedications() == null ? new ArrayList<>() : model.getMedications());
        mr.setAllergies(model.getAllergies() == null ? new ArrayList<>() : model.getAllergies());
        return mr;
    }

    /**
     * Build an entity from RestPersonModel with default
     */
    public static MedicalRecord fromModelAndDefault(RestMedicalRecordModel model, MedicalRecord defaultObject) {
        MedicalRecord mr = new MedicalRecord();
        mr.setBirthdate(model.getBirthdate() != null ? model.getBirthdate() : defaultObject.getBirthdate());
        mr.setMedications(
                (model.getMedications() == null || model.getMedications().isEmpty())
                        ? defaultObject.getMedications()
                        : model.getMedications());
        mr.setAllergies(
                (model.getAllergies() == null || model.getAllergies().isEmpty())
                        ? defaultObject.getAllergies()
                        : model.getAllergies());
        return mr;
    }
}
