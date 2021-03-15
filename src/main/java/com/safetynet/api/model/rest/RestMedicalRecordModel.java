package com.safetynet.api.model.rest;

import lombok.*;

import java.util.List;

/**
 * Model for the Person entity : POST PUT
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RestMedicalRecordModel {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

    public boolean isEmpty() {
        return birthdate == null
                && (medications == null || medications.isEmpty())
                && (allergies == null || allergies.isEmpty());
    }
}
