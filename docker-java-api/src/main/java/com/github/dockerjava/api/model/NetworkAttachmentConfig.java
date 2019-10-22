package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NetworkAttachmentConfig implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Target")
    private String target;

    /**
     * @since 1.24
     */
    @JsonProperty("Aliases")
    private List<String> aliases;

    /**
     * @see #target
     */
    @CheckForNull
    public String getTarget() {
        return target;
    }

    /**
     * @see #target
     */
    public NetworkAttachmentConfig withTarget(String target) {
        this.target = target;
        return this;
    }

    /**
     * @see #aliases
     */
    @CheckForNull
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * @see #aliases
     */
    public NetworkAttachmentConfig withAliases(List<String> aliases) {
        this.aliases = aliases;
        return this;
    }

    @Override
    public String toString() {
        return "NetworkAttachmentConfig{" +
                "target='" + target + '\'' +
                ", aliases=" + aliases +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkAttachmentConfig that = (NetworkAttachmentConfig) o;
        return Objects.equals(target, that.target) &&
                Objects.equals(aliases, that.aliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target, aliases);
    }
}
