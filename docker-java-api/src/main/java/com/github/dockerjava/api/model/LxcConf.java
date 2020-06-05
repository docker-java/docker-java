package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LxcConf implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Key")
    public String key;

    @JsonProperty("Value")
    public String value;

    public LxcConf(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public LxcConf() {
    }

    public String getKey() {
        return key;
    }

    public LxcConf setKey(String key) {
        this.key = key;
        return this;
    }

    public String getValue() {
        return value;
    }

    public LxcConf setValue(String value) {
        this.value = value;
        return this;
    }

}
