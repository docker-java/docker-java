package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * docker configs that will be exposed to the service
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode
@ToString
public class ContainerSpecConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty("File")
    private ContainerSpecFile file;

    @JsonProperty("ConfigID")
    private String configID;

    @JsonProperty("ConfigName")
    private String configName;

    public ContainerSpecFile getFile() {
        return file;
    }

    public ContainerSpecConfig withFile(ContainerSpecFile file) {
        this.file = file;
        return this;
    }

    public String getConfigID() {
        return configID;
    }

    public ContainerSpecConfig withConfigID(String configID) {
        this.configID = configID;
        return this;
    }

    public String getConfigName() {
        return configName;
    }

    public ContainerSpecConfig withConfigName(String configName) {
        this.configName = configName;
        return this;
    }
}
