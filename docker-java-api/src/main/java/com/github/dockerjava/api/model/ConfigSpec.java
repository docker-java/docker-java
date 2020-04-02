package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Base64;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
@EqualsAndHashCode
@ToString
public class ConfigSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.30
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since 1.30
     */
    @JsonProperty("Data")
    private String data;

    /**
     * @since 1.30
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    /**
     * @see #name
     */
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public ConfigSpec withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #data
     */
    public byte[] getData() {
        return Base64.getDecoder().decode(data);
    }

    /**
     * @see #data
     */
    public ConfigSpec withData(byte[] data) {
        this.data = Base64.getEncoder().encodeToString(data);
        return this;
    }

    /**
     * @see #labels
     */
    public ConfigSpec withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }
}
