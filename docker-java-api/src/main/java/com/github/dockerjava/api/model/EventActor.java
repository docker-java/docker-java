package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

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
        return "EventActor{" +
                "id='" + id + '\'' +
                ", attributes=" + attributes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventActor that = (EventActor) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, attributes);
    }
}
