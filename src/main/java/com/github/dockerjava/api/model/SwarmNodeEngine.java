package com.github.dockerjava.api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SwarmNodeEngine {

    @JsonProperty("EngineVersion")
    private String engineVersion;

    @JsonProperty("Labels")
    private Map<String,String> labels;

    @JsonProperty("Plugins")
    private SwarmNodePlugin[] plugins;

    public String getEngineVersion() {
        return engineVersion;
    }

    public Map<String, String> getLabels() {
        return labels;
    }

    public SwarmNodePlugin[] getPlugins() {
        return plugins;
    }

    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    public void setLabels(Map<String, String> labels) {
        this.labels = labels;
    }

    public void setPlugins(SwarmNodePlugin[] plugins) {
        this.plugins = plugins;
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
