package com.safetynet.api.model.post;

import com.fasterxml.jackson.annotation.JsonView;
import com.safetynet.api.util.Views;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostResultModel {

    @JsonView(Views.PostResultModel.class)
    boolean success;

    @JsonView(Views.PostResultModel.class)
    String entityClass;

    @JsonView(Views.PostResultModel.class)
    int count;

    @JsonView(Views.PostResultModel.class)
    String errorMessage;

    public static PostResultModel buildSuccess(String entityClass, int count) {
        PostResultModel model = new PostResultModel();
        model.setSuccess(true);
        model.setEntityClass(entityClass);
        model.setCount(count);
        model.setErrorMessage(null);
        return model;
    }

    public static PostResultModel buildError(String entityClass, String errorMessage) {
        PostResultModel model = new PostResultModel();
        model.setSuccess(false);
        model.setEntityClass(entityClass);
        model.setCount(-1);
        model.setErrorMessage(errorMessage);
        return model;
    }
}
