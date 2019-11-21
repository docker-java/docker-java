package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FromPrimitive;
import com.github.dockerjava.api.annotation.ToPrimitive;

import java.io.Serializable;

public enum Isolation implements Serializable {
    DEFAULT("default"),

    PROCESS("process"),

    HYPERV("hyperv");

    private String value;

    Isolation(String value) {
        this.value = value;
    }

    @ToPrimitive
    public String getValue() {
        return value;
    }

    @FromPrimitive
    public static Isolation fromValue(String text) {
        for (Isolation b : Isolation.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
