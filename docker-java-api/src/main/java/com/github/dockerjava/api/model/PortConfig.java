package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
public class PortConfig implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Name")
    private String name;

    /**
     * @since 1.24
     */
    @JsonProperty("Protocol")
    private PortConfigProtocol protocol;

    /**
     * @since 1.24
     */
    @JsonProperty("TargetPort")
    private int targetPort;

    /**
     * @since 1.24
     */
    @JsonProperty("PublishedPort")
    private int publishedPort;

    /**
     * @since 1.25
     * docker 1.13
     * https://github.com/mrjana/docker/blob/14ac9f60d0174256e0713701ebffaf5ca827da71/api/types/swarm/network.go
     */
    @JsonProperty("PublishMode")
    private PublishMode publishMode;

    /**
     * @see #name
     */
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public PortConfig withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * @see #protocol
     */
    @CheckForNull
    public PortConfigProtocol getProtocol() {
        return protocol;
    }

    /**
     * @see #protocol
     */
    public PortConfig withProtocol(PortConfigProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    /**
     * @see #targetPort
     */
    @CheckForNull
    public int getTargetPort() {
        return targetPort;
    }

    /**
     * @see #targetPort
     */
    public PortConfig withTargetPort(int targetPort) {
        this.targetPort = targetPort;
        return this;
    }

    /**
     * @see #publishedPort
     */
    @CheckForNull
    public int getPublishedPort() {
        return publishedPort;
    }

    /**
     * @see #publishedPort
     */
    public PortConfig withPublishedPort(int publishedPort) {
        this.publishedPort = publishedPort;
        return this;
    }

    @CheckForNull
    public PublishMode getPublishMode() {
        return publishMode;
    }

    public PortConfig withPublishMode(PublishMode publishMode) {
        this.publishMode = publishMode;
        return this;
    }

    @Override
    public String toString() {
        return "PortConfig{" +
                "name='" + name + '\'' +
                ", protocol=" + protocol +
                ", targetPort=" + targetPort +
                ", publishedPort=" + publishedPort +
                ", publishMode=" + publishMode +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortConfig that = (PortConfig) o;
        return targetPort == that.targetPort &&
                publishedPort == that.publishedPort &&
                Objects.equals(name, that.name) &&
                protocol == that.protocol &&
                publishMode == that.publishMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, protocol, targetPort, publishedPort, publishMode);
    }

    public enum PublishMode {
        //ingress load balancing using routing mesh.
        @JsonProperty("ingress")
        ingress,
        //direct host level access on the host where the task is running.
        @JsonProperty("host")
        host
    }
}
