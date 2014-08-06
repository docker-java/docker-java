package com.github.dockerjava.client.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
