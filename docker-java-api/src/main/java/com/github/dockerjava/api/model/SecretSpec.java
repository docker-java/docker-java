package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Base64;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
@EqualsAndHashCode
@ToString
public class SecretSpec implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.25
     */
    @FieldName("Name")
    private String name;

    /**
     * @since 1.25
     */
    @FieldName("Data")
    private String data;

    /**
     * @since 1.25
     */
    @FieldName("Labels")
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
        this.data = Base64.getEncoder().encodeToString(data.getBytes());
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
}
