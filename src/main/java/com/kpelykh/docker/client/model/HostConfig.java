package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class HostConfig {

    @JsonProperty("Binds")
    public String[] binds;

    @JsonProperty("ContainerIDFile")
    public String containerIDFile;

    @JsonProperty("LxcConf")
    public String[] lxcConf = new String[]{};

    public HostConfig(String[] binds) {
        this.binds = binds;
    }

    public String[] getBinds() {
        return binds;
    }

    public void setBinds(String[] binds) {
        this.binds = binds;
    }

    public String getContainerIDFile() {
        return containerIDFile;
    }

    public void setContainerIDFile(String containerIDFile) {
        this.containerIDFile = containerIDFile;
    }

    public String[] getLxcConf() {
        return lxcConf;
    }

    public void setLxcConf(String[] lxcConf) {
        this.lxcConf = lxcConf;
    }
}
