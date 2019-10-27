package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * Part of {@link Version}
 *
 * @since {@link RemoteApiVersion#VERSION_1_35}
 * @author Dmitry Tretyakov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionComponent implements Serializable {
    public static final long serialVersionUID = 1L;

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
        return "VersionComponent{" +
                "details=" + details +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionComponent that = (VersionComponent) o;
        return Objects.equals(details, that.details) &&
                Objects.equals(name, that.name) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(details, name, version);
    }
}
