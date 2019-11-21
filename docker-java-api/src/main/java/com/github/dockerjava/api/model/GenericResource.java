package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;

import java.io.Serializable;

public abstract class GenericResource<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("Kind")
    String kind;
    @FieldName("Value")
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
