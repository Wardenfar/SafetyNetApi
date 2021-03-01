package com.safetynet.api.model.delete;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class DeletePersonModel {

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

}
