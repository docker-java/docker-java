package com.github.dockerjava.api.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Log driver to use for a created/running container. The
 * available types are:
 * 
 *     json-file (default)
 *     syslog
 *     journald
 *     none
 *
 * If a driver is specified that is NOT supported,docker
 * will default to null. If configs are supplied that are
 * not supported by the type docker will ignore them. In most 
 * cases setting the config option to null will suffice. Consult 
 * the docker remote API for a more detailed and up-to-date 
 * explanation of the available types and their options.
 */
public class LogConfig {
    
    @JsonProperty("Type")
    public String type;
    
    @JsonProperty("Config")
    public Map<String, String> config;

    public LogConfig(String type, Map<String, String> config) {
        this.type = type;
        this.config = config;
    }

    public LogConfig() {
    }

    public String getType() {
        return type;
    }

    public LogConfig setType(String type) {
        this.type = type;
        return this;
    }

    @JsonIgnore
    public Map<String, String> getConfig() {
    	return config;
    }
    
    @JsonIgnore
    public LogConfig setConfig(Map<String, String> config) {
    	this.config = config;
    	return this;
    }
}
