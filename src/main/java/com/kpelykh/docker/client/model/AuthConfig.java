package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class AuthConfig {
	@JsonProperty
	private String username;
	@JsonProperty
	private String password;
	@JsonProperty
	private String email;
	@JsonProperty("serveraddress")
	private String serverAddress = "https://index.docker.io/v1/";

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
