package com.github.dockerjava.api.command;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * The response of a {@link CreateSecretCmd}
 */
@EqualsAndHashCode
@ToString
public class CreateSecretResponse {
    @FieldName("ID")
    private String id;

    public String getId() {
        return id;
    }
}
