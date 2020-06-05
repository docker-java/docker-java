package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @author Yuting Liu
 * @see Container
 */
@EqualsAndHashCode
@ToString
public class ContainerMount implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("Name")
    String name;

    @JsonProperty("Source")
    String source;

    @JsonProperty("Destination")
    String destination;

    @JsonProperty("Driver")
    String driver;

    @JsonProperty("Mode")
    String mode;

    @JsonProperty("RW")
    boolean rw;

    @JsonProperty("Propagation")
    String propagation;

    /**
     * @see #name
     */
    @CheckForNull
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public ContainerMount withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #source
     */
    @CheckForNull
    public String getSource() {
        return source;
    }

    /**
     * @see #source
     */
    public ContainerMount withSource(String source) {
        this.source = source;
        return this;
    }

    /**
     * @see #destination
     */
    @CheckForNull
    public String getDestination() {
        return destination;
    }

    /**
     * @see #destination
     */
    public ContainerMount withDestination(String destination) {
        this.destination = destination;
        return this;
    }

    /**
     * @see #driver
     */
    @CheckForNull
    public String getDriver() {
        return driver;
    }

    /**
     * @see #driver
     */
    public ContainerMount withDriver(String driver) {
        this.driver = driver;
        return this;
    }

    /**
     * @see #mode
     */
    @CheckForNull
    public String getMode() {
        return driver;
    }

    /**
     * @see #mode
     */
    public ContainerMount withMode(String mode) {
        this.mode = mode;
        return this;
    }

    /**
     * @see #rw
     */
    @CheckForNull
    public Boolean getRw() {
        return rw;
    }

    /**
     * @see #rw
     */
    public ContainerMount withRw(Boolean rw) {
        this.rw = rw;
        return this;
    }

    /**
     * @see #propagation
     */
    @CheckForNull
    public String getPropagation() {
        return propagation;
    }

    /**
     * @see #propagation
     */
    public ContainerMount withPropagation(String propagation) {
        this.propagation = propagation;
        return this;
    }
}
