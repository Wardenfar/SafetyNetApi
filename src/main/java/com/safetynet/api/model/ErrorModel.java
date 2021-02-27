package com.safetynet.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ErrorModel {

    private int code;
    private String message;

    public static ErrorModel build(int code, String message) {
        ErrorModel model = new ErrorModel();
        model.setCode(code);
        model.setMessage(message);
        return model;
    }
}
