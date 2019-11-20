package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.io.Serializable;

public class AuthResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.23
     */
    @JsonProperty("Status")
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
