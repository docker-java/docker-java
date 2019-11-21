package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Parse reponses from /images/create
 *
 * @author Ryan Campbell (ryan.campbell@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class CreateImageResponse {

    @FieldName("status")
    private String id;

    public String getId() {
        return id;
    }
}
