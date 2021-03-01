package com.safetynet.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom Exception thrown when an entity was not Found
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="The entity was not found !")
public class EntityNotFound extends Exception {

    public EntityNotFound(String message) {
        super(message);
    }
}
