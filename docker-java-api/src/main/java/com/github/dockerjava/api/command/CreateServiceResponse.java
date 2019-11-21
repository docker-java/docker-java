package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The response of a {@link CreateServiceCmd}
 */
@EqualsAndHashCode
@ToString
public class CreateServiceResponse {
    @FieldName("ID")
    private String id;

    public String getId() {
        return id;
    }
}
