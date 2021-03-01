package com.safetynet.api.model.post;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PostPersonModel {

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
