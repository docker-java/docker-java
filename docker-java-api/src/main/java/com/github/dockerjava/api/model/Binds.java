package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Binds implements Serializable {
    private static final long serialVersionUID = 1L;

    private Bind[] binds;

    public Binds(Bind... binds) {
        this.binds = binds;
    }

    public Bind[] getBinds() {
        return binds;
    }

    @JsonValue
    public String[] toPrimitive() {
        return Stream.of(binds).map(Bind::toString).toArray(String[]::new);
    }

    @JsonCreator
    public static Binds fromPrimitive(String[] binds) {
        return new Binds(
                Stream.of(binds).map(Bind::parse).toArray(Bind[]::new)
        );
    }

}
