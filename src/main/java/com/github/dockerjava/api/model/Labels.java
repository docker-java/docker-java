package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Labels {

    @JsonProperty("executiondriver")
    private String executiondriver;

    @JsonProperty("kernelversion")
    private String kernelversion;

    @JsonProperty("operatingsystem")
    private String operatingsystem;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("storagedriver")
    private String storagedriver;

    public String getExecutiondriver() {
        return executiondriver;
    }

    public void setExecutiondriver(String executiondriver) {
        this.executiondriver = executiondriver;
    }

    public String getKernelversion() {
        return kernelversion;
    }

    public void setKernelversion(String kernelversion) {
        this.kernelversion = kernelversion;
    }

    public String getOperatingsystem() {
        return operatingsystem;
    }

    public void setOperatingsystem(String operatingsystem) {
        this.operatingsystem = operatingsystem;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getStoragedriver() {
        return storagedriver;
    }

    public void setStoragedriver(String storagedriver) {
        this.storagedriver = storagedriver;
    }

}
