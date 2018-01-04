package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IoRecursiveConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("major")
    private Long major;

    @JsonProperty("minor")
    private Long minor;

    @JsonProperty("op")
    private String op;

    @JsonProperty("value")
    private Long value;

    /**
     * @see #major
     */
    @CheckForNull
    public Long getMajor() {
        return major;
    }

    /**
     * @see #minor
     */
    @CheckForNull
    public Long getMinor() {
        return minor;
    }

    /**
     * @see #op
     */
    public String getOp() {
        return op;
    }

    /**
     * @see #value
     */
    public Long getValue() {
        return value;
    }

    /**
     * @see #major
     */
    public IoRecursiveConfig withMajor(Long major) {
        this.major = major;
        return this;
    }

    /**
     * @see #minor
     */
    public IoRecursiveConfig withMinor(Long minor) {
        this.minor = minor;
        return this;
    }

    /**
     * @see #op
     */
    public IoRecursiveConfig withOp(String op) {
        this.op = op;
        return this;
    }

    /**
     * @see #value
     */
    public IoRecursiveConfig withValue(Long value) {
        this.value = value;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
