package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

/**
 * Specification for DNS related configurations in resolver configuration file (&#x60;resolv.conf&#x60;).
 *
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerDNSConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Nameservers")
    private List<String> nameservers;
    @JsonProperty("Search")
    private List<String> search;
    @JsonProperty("Options")
    private List<String> options;

    public List<String> getNameservers() {
        return nameservers;
    }

    public ContainerDNSConfig withNameservers(List<String> nameservers) {
        this.nameservers = nameservers;
        return this;
    }

    public List<String> getSearch() {
        return search;
    }

    public ContainerDNSConfig withSearch(List<String> search) {
        this.search = search;
        return this;
    }

    public List<String> getOptions() {
        return options;
    }

    public ContainerDNSConfig withOptions(List<String> options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
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
