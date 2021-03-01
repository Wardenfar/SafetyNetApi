package com.safetynet.api.model.rest;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class RestPersonModel {

    @NonNull
    private String fireStation;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String address;

    @NonNull
    private String city;

    @NonNull
    private String zip;

    @NonNull
    private String phone;

    @NonNull
    private String email;
}
