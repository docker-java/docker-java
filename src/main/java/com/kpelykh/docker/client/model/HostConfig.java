package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class HostConfig {

    @JsonProperty("Binds")
    private String[] binds;

    @JsonProperty("ContainerIDFile")
    private String containerIDFile;

    @JsonProperty("LxcConf")
    private LxcConf[] lxcConf;

    public HostConfig(String[] binds) {
        this.binds = binds;
    }

    public String[] getBinds() {
        return binds;
    }

    public HostConfig setBinds(String[] binds) {
        this.binds = binds;
        return this;
    }

    public String getContainerIDFile() {
        return containerIDFile;
    }

    public HostConfig setContainerIDFile(String containerIDFile) {
        this.containerIDFile = containerIDFile;
        return this;
    }

    public LxcConf[] getLxcConf() {
        return lxcConf;
    }

    public HostConfig setLxcConf(LxcConf[] lxcConf) {
        this.lxcConf = lxcConf;
        return this;
    }

    public class LxcConf {
        @JsonProperty("Key")
        public String key;

        @JsonProperty("Value")
        public String value;

        public LxcConf(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public LxcConf() {
        }

        public String getKey() {
            return key;
        }

        public LxcConf setKey(String key) {
            this.key = key;
            return this;
        }

        public String getValue() {
            return value;
        }

        public LxcConf setValue(String value) {
            this.value = value;
            return this;
        }

    }
}
