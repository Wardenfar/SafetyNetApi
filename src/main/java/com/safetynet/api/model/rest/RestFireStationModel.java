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
public class RestFireStationModel {

    private String station;
    private String address;

    public boolean isEmpty() {
        return address == null && station == null;
    }
}
