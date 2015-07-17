package com.github.dockerjava.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LogConfig {
    
    @JsonProperty("Type")
    public String type = "json-file";
    
    @JsonProperty("Config")
    public List<String> config = null;

    public LogConfig(String type) {
        this.type = type;
    }

    public LogConfig() {
    }

    public String getType() {
        return type;
    }
    
    @JsonIgnore
    public List<String> getConfig() {
    	return config;
    }

    public LogConfig setType(String type) {
        this.type = type;
        return this;
    }
}
