package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Kanstantsin Shautsou
 * @see Container
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContainerPort implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("PrivatePort")
    private Integer privatePort;

    @JsonProperty("PublicPort")
    private Integer publicPort;

    @JsonProperty("Type")
    private String type;

    /**
     * @see #ip
     */
    @CheckForNull
    public String getIp() {
        return ip;
    }

    /**
     * @see #ip
     */
    public ContainerPort withIp(String ip) {
        this.ip = ip;
        return this;
    }

    /**
     * @see #privatePort
     */
    @CheckForNull
    public Integer getPrivatePort() {
        return privatePort;
    }

    /**
     * @see #privatePort
     */
    public ContainerPort withPrivatePort(Integer privatePort) {
        this.privatePort = privatePort;
        return this;
    }

    /**
     * @see #publicPort
     */
    @CheckForNull
    public Integer getPublicPort() {
        return publicPort;
    }

    /**
     * @see #publicPort
     */
    public ContainerPort withPublicPort(Integer publicPort) {
        this.publicPort = publicPort;
        return this;
    }

    /**
     * @see #type
     */
    @CheckForNull
    public String getType() {
        return type;
    }

    /**
     * @see #type
     */
    public ContainerPort withType(String type) {
        this.type = type;
        return this;
    }

    @Override
    public String toString() {
        return "ContainerPort{" +
                "ip='" + ip + '\'' +
                ", privatePort=" + privatePort +
                ", publicPort=" + publicPort +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContainerPort that = (ContainerPort) o;
        return Objects.equals(ip, that.ip) &&
                Objects.equals(privatePort, that.privatePort) &&
                Objects.equals(publicPort, that.publicPort) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, privatePort, publicPort, type);
    }
}
