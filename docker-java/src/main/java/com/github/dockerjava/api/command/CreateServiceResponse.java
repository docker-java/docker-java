package com.github.dockerjava.api.command;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * The response of a {@link CreateServiceCmd}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateServiceResponse {
    @JsonProperty("ID")
    private String id;

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
