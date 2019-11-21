package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * @author Kanstantsin Shautsou
 * @see Container
 */
@EqualsAndHashCode
@ToString
public class ContainerPort implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("IP")
    private String ip;

    @FieldName("PrivatePort")
    private Integer privatePort;

    @FieldName("PublicPort")
    private Integer publicPort;

    @FieldName("Type")
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
}
