package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecretSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.25
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since 1.25
     */
    @JsonProperty("Data")
    private String data;

    /**
     * @since 1.25
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
    public SecretSpec withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #data
     */
    public String getData() {
        return data;
    }

    /**
     * @see #data
     */
    public SecretSpec withData(String data) {
        this.data = Base64.encodeBase64String(data.getBytes());
        return this;
    }

    /**
     * @see #labels
     */
    public SecretSpec withLabels(Map<String, String> labels) {
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

    @Override
    public String toString() {
        return "SecretSpec{" +
                "name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", labels=" + labels +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SecretSpec that = (SecretSpec) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(data, that.data) &&
                Objects.equals(labels, that.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, data, labels);
    }
}
