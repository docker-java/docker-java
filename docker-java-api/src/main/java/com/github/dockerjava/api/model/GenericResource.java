package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@EqualsAndHashCode
@ToString
public abstract class GenericResource<T> extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Kind")
    String kind;
    @JsonProperty("Value")
    T value = null;

    public String getKind() {
        return kind;
    }

    public GenericResource withKind(String kind) {
        this.kind = kind;
        return this;
    }

    public T getValue() {
        return value;
    }

    public GenericResource withValue(T value) {
        this.value = value;
        return this;
    }
}
