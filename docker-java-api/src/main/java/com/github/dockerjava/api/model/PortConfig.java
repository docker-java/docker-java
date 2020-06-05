package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@EqualsAndHashCode
@ToString
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

    public enum PublishMode {
        //ingress load balancing using routing mesh.
        @JsonProperty("ingress")
        ingress,
        //direct host level access on the host where the task is running.
        @JsonProperty("host")
        host
    }
}
