package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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
        return "ContainerDNSConfig{" +
                "nameservers=" + nameservers +
                ", search=" + search +
                ", options=" + options +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerDNSConfig that = (ContainerDNSConfig) o;
        return Objects.equals(nameservers, that.nameservers) &&
                Objects.equals(search, that.search) &&
                Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameservers, search, options);
    }
}
