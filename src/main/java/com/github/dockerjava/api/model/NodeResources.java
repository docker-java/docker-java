package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NodeResources {

    @JsonProperty("NanoCPUs")
    private Long nanoCPUs;

    @JsonProperty("MemoryBytes")
    private Long memoryBytes;

    public Long getNanoCPUs() {
        return nanoCPUs;
    }

    public Long getMemoryBytes() {
        return memoryBytes;
    }

    public void setNanoCPUs(Long nanoCPUs) {
        this.nanoCPUs = nanoCPUs;
    }

    public void setMemoryBytes(Long memoryBytes) {
        this.memoryBytes = memoryBytes;
    }
}
