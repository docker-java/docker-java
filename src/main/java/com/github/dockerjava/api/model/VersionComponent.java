package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Part of {@link Version}
 *
 * @since {@link RemoteApiVersion#VERSION_1_35}
 * @author Dmitry Tretyakov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionComponent implements Serializable {
    public static final Long serialVersionUID = 1L;

    @JsonProperty("Details")
    private Map<String, String> details;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Version")
    private String version;

    /**
     * @see #details
     */
    @CheckForNull
    public Map<String, String> getDetails() {
        return details;
    }

    /**
     * @see #details
     */
    public VersionComponent withDetails(Map<String, String> details) {
        this.details = details;
        return this;
    }

    /**
     * @see #name
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public VersionComponent withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #version
     */
    @CheckForNull
    public String getVersion() {
        return version;
    }

    /**
     * @see #version
     */
    public VersionComponent withVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
