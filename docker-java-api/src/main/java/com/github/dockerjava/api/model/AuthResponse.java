package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true)
public class AuthResponse extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.23
     */
    @JsonProperty("Status")
    @ToString.Include
    private String status;

    /**
     * @since 1.23
     */
    @JsonProperty("IdentityToken")
    private String identityToken;

    /**
     * @see #status
     */
    @Nonnull
    public String getStatus() {
        return status;
    }

    /**
     * @see #identityToken
     */
    @CheckForNull
    public String getIdentityToken() {
        return identityToken;
    }
}
