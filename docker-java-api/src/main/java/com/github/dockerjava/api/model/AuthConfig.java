package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

@EqualsAndHashCode
@ToString
public class AuthConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * For backwards compatibility. Make sure you update the properties if you change this.
     *
     * @see "/docker.io.properties"
     */
    public static final String DEFAULT_SERVER_ADDRESS = "https://index.docker.io/v1/";

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("email")
    private String email;

    @JsonProperty("serveraddress")
    private String registryAddress = DEFAULT_SERVER_ADDRESS;

    @JsonProperty("auth")
    private String auth;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("registrytoken")
    private String registrytoken;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_23}
     */
    @JsonProperty("identitytoken")
    private String identitytoken;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("stackOrchestrator")
    private String stackOrchestrator;

    public String getUsername() {
        return username;
    }

    public AuthConfig withUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthConfig withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AuthConfig withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public AuthConfig withRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
        return this;
    }

    public String getAuth() {
        return auth;
    }

    public AuthConfig withAuth(String auth) {
        this.auth = auth;
        return this;
    }

    /**
     * @see #identitytoken
     */
    public String getIdentitytoken() {
        return identitytoken;
    }
    /**
     * @see #identitytoken
     */
    public AuthConfig withIdentityToken(String identitytoken) {
        this.identitytoken = identitytoken;
        return this;
    }

    /**
     * @see #registrytoken
     */
    @CheckForNull
    public String getRegistrytoken() {
        return registrytoken;
    }

    /**
     * @see #registrytoken
     */
    public AuthConfig withRegistrytoken(String registrytoken) {
        this.registrytoken = registrytoken;
        return this;
    }

    /**
     * @see #stackOrchestrator
     */
    public String getStackOrchestrator() {
        return stackOrchestrator;
    }

    /**
     * @see #stackOrchestrator
     */
    public void setStackOrchestrator(String stackOrchestrator) {
        this.stackOrchestrator = stackOrchestrator;
    }
}
