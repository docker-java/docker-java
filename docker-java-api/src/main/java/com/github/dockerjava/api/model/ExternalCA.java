package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
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
}
