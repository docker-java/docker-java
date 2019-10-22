package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalCA implements Serializable {

    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Protocol")
    private ExternalCAProtocol protocol;

    /**
     * @since 1.24
     */
    @JsonProperty("URL")
    private String url;

    /**
     * @since 1.24
     */
    @JsonProperty("Options")
    private Map<String, String> options;

    /**
     * @see #protocol
     */
    @CheckForNull
    public ExternalCAProtocol getProtocol() {
        return protocol;
    }

    /**
     * @see #protocol
     */
    public ExternalCA withProtocol(ExternalCAProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * @see #url
     */
    @CheckForNull
    public String getUrl() {
        return url;
    }

    /**
     * @see #url
     */
    public ExternalCA withUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @see #options
     */
    @CheckForNull
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * @see #options
     */
    public ExternalCA withOptions(Map<String, String> options) {
        this.options = options;
        return this;
    }

    @Override
    public String toString() {
        return "ExternalCA{" +
                "protocol=" + protocol +
                ", url='" + url + '\'' +
                ", options=" + options +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExternalCA that = (ExternalCA) o;
        return protocol == that.protocol &&
                Objects.equals(url, that.url) &&
                Objects.equals(options, that.options);
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocol, url, options);
    }
}
