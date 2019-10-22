package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * docker configs that will be exposed to the service
 *
 * @since {@link RemoteApiVersion#VERSION_1_29}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
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

    @Override
    public String toString() {
        return "ContainerSpecConfig{" +
                "file=" + file +
                ", configID='" + configID + '\'' +
                ", configName='" + configName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerSpecConfig that = (ContainerSpecConfig) o;
        return Objects.equals(file, that.file) &&
                Objects.equals(configID, that.configID) &&
                Objects.equals(configName, that.configName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, configID, configName);
    }
}
