package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Part of {@link Version}
 *
 * @since {@link RemoteApiVersion#VERSION_1_35}
 * @author Dmitry Tretyakov
 */
@EqualsAndHashCode
@ToString
public class VersionComponent implements Serializable {
    public static final long serialVersionUID = 1L;

    @FieldName("Details")
    private Map<String, String> details;

    @FieldName("Name")
    private String name;

    @FieldName("Version")
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
}
