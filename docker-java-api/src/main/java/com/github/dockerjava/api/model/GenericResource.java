package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.WRAPPER_OBJECT
)
@JsonSubTypes({@JsonSubTypes.Type(
    value = NamedResourceSpec.class,
    name = "NamedResourceSpec"
), @JsonSubTypes.Type(
    value = DiscreteResourceSpec.class,
    name = "DiscreteResourceSpec"
)})
@EqualsAndHashCode
@ToString
public abstract class GenericResource<T> extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Kind")
    String kind;

    /**
     * @see #kind
     */
    @CheckForNull
    public String getKind() {
        return kind;
    }

    /**
     * @see #kind
     */
    public GenericResource<T> withKind(String kind) {
        this.kind = kind;
        return this;
    }

    public abstract T getValue();

    public abstract GenericResource<T> withValue(T value);
}
