package com.kpelykh.docker.client.model;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ContainerConfig {

    @JsonProperty("Hostname")     public String hostName;
    @JsonProperty("PortSpecs")    public String portSpecs;
    @JsonProperty("User")         public String user;
    @JsonProperty("Tty")          public boolean tty;
    @JsonProperty("OpenStdin")    public boolean stdinOpen;
    @JsonProperty("StdinOnce")    public boolean stdInOpce;
    @JsonProperty("Memory")       public long    memoryLimit;
    @JsonProperty("MemorySwap")   public long    memorySwap;
    @JsonProperty("CpuShares")    public long    cpuShares;
    @JsonProperty("AttachStdin")  public boolean attachStdin;
    @JsonProperty("AttachStdout") public boolean attachStdout;
    @JsonProperty("AttachStderr") public boolean attachStderr;
    @JsonProperty("Env")          public String  env;
    @JsonProperty("Cmd")          public String[]  cmd;
    @JsonProperty("Dns")          public String[]  dns;
    @JsonProperty("Image")        public String  image;
    @JsonProperty("Volumes")      public Object  volumes;
    @JsonProperty("VolumesFrom")  public String  volumesFrom;

    public ContainerConfig() {}

    /*
     *
     * Builder Example
     *
       ContainerConfig containerConfig =
            new ContainerConfig.Builder("busybox")
                    .cmd(new String[] {"true"}).build();
                        .hostName(hostname)
                        .user(user)
                        .stdinOpen(stdinOpen)
                        .tty(tty)
                        .memoryLimit(memLimit)
                        .portSpecs(ports)
                        .env(environment)
                        .dns(dns)
                        .volumes(volumes)
                        .volumesFrom(volumesFrom);
      */


    private ContainerConfig(Builder builder) {
        hostName = builder.hostName;
        portSpecs = builder.portSpecs;
        user = builder.user;
        tty = builder.tty;
        cpuShares = builder.cpuShares;
        stdinOpen = builder.stdinOpen;
        memoryLimit = builder.memoryLimit;
        attachStdin = builder.attachStdin;
        attachStdout = builder.attachStdout;
        attachStderr = builder.attachStderr;
        env = builder.env;
        cmd = builder.cmd;
        dns = builder.dns;
        image = builder.image;
        volumes = builder.volumes;
        volumesFrom = builder.volumesFrom;
    }

    public static class Builder {
        public String hostName;
        public String portSpecs;
        public String user;
        public boolean tty;
        public boolean openStdin;
        public boolean stdinOpen;
        public long    cpuShares;
        public long    memoryLimit;
        public long    memorySwap;
        public boolean attachStdin;
        public boolean attachStdout;
        public boolean attachStderr;
        public String  env;
        public String[] cmd;
        public String[] dns;
        public String  image;
        public Object  volumes;
        public String  volumesFrom;

        public Builder(String image) {
            this.image = image;
        }

        public Builder portSpecs(String portSpecs) {
            this.portSpecs = portSpecs;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder tty(boolean tty) {
            this.tty = tty;
            return this;
        }

        public Builder stdinOpen(boolean stdinOpen) {
            this.stdinOpen = stdinOpen;
            return this;
        }

        public Builder memoryLimit(long memoryLimit) {
            this.memoryLimit = memoryLimit;
            return this;
        }

        public Builder attachStdin(boolean attachStdin) {
            this.attachStdin = attachStdin;
            return this;
        }

        public Builder attachStdout(boolean attachStdout) {
            this.attachStdout = attachStdout;
            return this;
        }

        public Builder attachStderr(boolean attachStderr) {
            this.attachStderr = attachStderr;
            return this;
        }

        public Builder env(String env) {
            this.env = env;
            return this;
        }

        public Builder cmd(String cmd) {
            this.cmd = StringUtils.split(cmd, " ");
            return this;
        }

        public Builder cmd(String[] cmd) {
            this.cmd = cmd;
            return this;
        }

        public Builder hostName(String hostName) {
            this.hostName = hostName;
            return this;
        }

        public Builder dns(String[] dns) {
            this.dns = dns;
            return this;
        }

        public Builder volumes(String volumes) {
            this.volumes = volumes;
            return this;
        }

        public Builder volumesFrom(String volumesFrom) {
            this.volumesFrom = volumesFrom;
            return this;
        }

        public Builder cpuShares(int cpuShares) {
            this.cpuShares = cpuShares;
            return this;
        }


        public ContainerConfig build() {
            return new ContainerConfig(this);
        }

    }

}
