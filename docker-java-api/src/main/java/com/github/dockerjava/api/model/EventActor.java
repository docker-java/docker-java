package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Kanstantsin Shautsou
 * @since 1.22
 */
public class EventActor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.22
     */
    @JsonProperty("ID")
    private String id;

    /**
     * @since 1.22
     */
    @JsonProperty("Attributes")
    private Map<String, String> attributes;

    /**
     * @see #id
     */
    @CheckForNull
    public String getId() {
        return id;
    }

    /**
     * @see #id
     */
    public EventActor withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * @see #attributes
     */
    @CheckForNull
    public Map<String, String> getAttributes() {
        return attributes;
    }

    /**
     * @see #attributes
     */
    public EventActor withAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
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
