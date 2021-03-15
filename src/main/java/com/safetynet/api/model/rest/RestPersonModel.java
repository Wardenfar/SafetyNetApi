package com.safetynet.api.model.rest;

import lombok.*;

/**
 * Model for the Person entity : POST PUT
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RestPersonModel {

    private String fireStation;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;

    public boolean isEmpty() {
        return address == null
                && city == null
                && zip == null
                && phone == null
                && email == null
                && fireStation == null;
    }
}
