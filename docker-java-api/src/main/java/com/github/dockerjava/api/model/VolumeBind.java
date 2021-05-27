package com.github.dockerjava.api.model;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
public class VolumeBind extends DockerObject implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String hostPath;

    private final String containerPath;

    public VolumeBind(String hostPath, String containerPath) {
        this.hostPath = hostPath;
        this.containerPath = containerPath;
    }

    public String getContainerPath() {
        return containerPath;
    }

    public String getHostPath() {
        return hostPath;
    }

    @Override
    public String toString() {
        return hostPath + ":" + containerPath;
    }
}
