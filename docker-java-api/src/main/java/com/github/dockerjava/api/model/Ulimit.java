package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author Vangie Du (duwan@live.com)
 */
public class Ulimit implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Soft")
    private Integer soft;

    @JsonProperty("Hard")
    private Integer hard;

    public Ulimit() {
    }

    public Ulimit(String name, int soft, int hard) {
        requireNonNull(name, "Name is null");
        this.name = name;
        this.soft = soft;
        this.hard = hard;
    }

    public String getName() {
        return name;
    }

    public Integer getSoft() {
        return soft;
    }

    public Integer getHard() {
        return hard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ulimit ulimit = (Ulimit) o;
        return Objects.equals(name, ulimit.name) &&
                Objects.equals(soft, ulimit.soft) &&
                Objects.equals(hard, ulimit.hard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, soft, hard);
    }
}
