package com.github.dockerjava.api.model;

import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthConfigurations {

	@JsonProperty("configs")
	private Map<String, AuthConfig> configs = new TreeMap<>();
	
	public void addConfig(AuthConfig authConfig){
		configs.put(authConfig.getServerAddress(), authConfig);
	}
	
	public Map<String, AuthConfig> getConfigs(){
		return this.configs;
	}
	
}
