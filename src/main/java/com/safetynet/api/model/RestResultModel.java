package com.safetynet.api.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The result model for an REST request
 */
@NoArgsConstructor
@Getter
@Setter
public class RestResultModel {

    @JsonView(Views.RestResultModel.class)
    String method;

    @JsonView(Views.RestResultModel.class)
    boolean success;

    @JsonView(Views.RestResultModel.class)
    String entityClass;

    @JsonView(Views.RestResultModel.class)
    String errorMessage;

    /**
     * Build a success Model
     */
    public static RestResultModel buildSuccess(String method, String entityClass) {
        RestResultModel model = new RestResultModel();
        model.setMethod(method);
        model.setSuccess(true);
        model.setEntityClass(entityClass);
        model.setErrorMessage(null);
        return model;
    }

    /**
     * Build an error Model
     */
    public static RestResultModel buildError(String method, String entityClass, String errorMessage) {
        RestResultModel model = new RestResultModel();
        model.setMethod(method);
        model.setSuccess(false);
        model.setEntityClass(entityClass);
        model.setErrorMessage(errorMessage);
        return model;
    }
}
