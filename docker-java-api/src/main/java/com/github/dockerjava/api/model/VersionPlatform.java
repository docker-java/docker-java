package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Part of {@link Version}
 *
 * @since {@link RemoteApiVersion#VERSION_1_35}
 * @author Dmitry Tretyakov
 */
@EqualsAndHashCode
@ToString
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
}
