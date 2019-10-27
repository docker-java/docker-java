package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Part of {@link Version}
 *
 * @since {@link RemoteApiVersion#VERSION_1_35}
 * @author Dmitry Tretyakov
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VersionPlatform implements Serializable {
    public static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

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
    public VersionPlatform withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "VersionPlatform{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionPlatform that = (VersionPlatform) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
