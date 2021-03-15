package com.safetynet.api.model.delete;

import lombok.*;

/**
 * Model for the Person entity : DELETE
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DeleteMedicalRecordModel {
    
    private String firstName;
    private String lastName;

}
