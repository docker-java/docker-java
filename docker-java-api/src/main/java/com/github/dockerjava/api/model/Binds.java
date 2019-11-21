package com.github.dockerjava.api.model;

import java.io.Serializable;
import java.util.stream.Stream;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

public class Binds implements Serializable {
    private static final long serialVersionUID = 1L;

    private Bind[] binds;

    public Binds(Bind... binds) {
        this.binds = binds;
    }

    public Bind[] getBinds() {
        return binds;
    }

    @ToPrimitive
    public String[] toPrimitive() {
        return Stream.of(binds).map(Bind::toString).toArray(String[]::new);
    }

    @FromPrimitive
    public static Binds fromPrimitive(String[] binds) {
        return new Binds(
                Stream.of(binds).map(Bind::parse).toArray(Bind[]::new)
        );
    }

}
