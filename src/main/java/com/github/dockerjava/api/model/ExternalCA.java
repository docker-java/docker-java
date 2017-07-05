package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalCA implements Serializable {

    public static final Long serialVersionUID = 1L;

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
