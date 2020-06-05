package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * Specification for DNS related configurations in resolver configuration file (&#x60;resolv.conf&#x60;).
 *
 * @since {@link RemoteApiVersion#VERSION_1_25}
 */
@EqualsAndHashCode
@ToString
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
}
