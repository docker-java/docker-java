package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthConfig {

    /**
     * For backwards compatibility. Make sure you update the properties if you change this.
     *
     * @see /docker.io.properties
     */
    public static final String DEFAULT_SERVER_ADDRESS = "https://index.docker.io/v1/";

    @JsonProperty
	private String username;
	
	@JsonProperty
	private String password;
	
	@JsonProperty
	private String email;
	
	@JsonProperty("serveraddress")
	private String serverAddress = DEFAULT_SERVER_ADDRESS;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Override
	public String toString() {
		return "AuthConfig{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", serverAddress='" + serverAddress + '\'' +
				'}';
	}
}
