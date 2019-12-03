package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@EqualsAndHashCode
@ToString
public class CreateContainerResponse {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Warnings")
    private String[] warnings;

    public String getId() {
        return id;
    }

    public String[] getWarnings() {
        return warnings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setWarnings(String[] warnings) {
        this.warnings = warnings;
    }
}
