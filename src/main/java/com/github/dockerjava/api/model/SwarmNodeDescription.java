package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmNodeDescription {
    @JsonProperty("Hostname")
    private String hostname;

    @JsonProperty("Platform")
    private SwarmNodePlatform platform;

    @JsonProperty("Resources")
    private SwarmNodeResources resources;

    @JsonProperty("Engine")
    private SwarmNodeEngine engine;

    public String getHostname() {
        return hostname;
    }

    public SwarmNodePlatform getPlatform() {
        return platform;
    }

    public SwarmNodeResources getResources() {
        return resources;
    }

    public SwarmNodeEngine getEngine() {
        return engine;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setPlatform(SwarmNodePlatform platform) {
        this.platform = platform;
    }

    public void setResources(SwarmNodeResources resources) {
        this.resources = resources;
    }

    public void setEngine(SwarmNodeEngine engine) {
        this.engine = engine;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
