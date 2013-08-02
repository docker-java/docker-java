package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Arrays;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ContainerConfig {

    @JsonProperty("Hostname")     private String    hostName;
    @JsonProperty("PortSpecs")    private String[]  portSpecs;
    @JsonProperty("User")         private String    user;
    @JsonProperty("Tty")          private boolean   tty;
    @JsonProperty("OpenStdin")    private boolean   stdinOpen;
    @JsonProperty("StdinOnce")    private boolean   stdInOnce;
    @JsonProperty("Memory")       private long      memoryLimit;
    @JsonProperty("MemorySwap")   private long      memorySwap;
    @JsonProperty("CpuShares")    private int       cpuShares;
    @JsonProperty("AttachStdin")  private boolean   attachStdin;
    @JsonProperty("AttachStdout") private boolean   attachStdout;
    @JsonProperty("AttachStderr") private boolean   attachStderr;
    @JsonProperty("Env")          private String[] env;
    @JsonProperty("Cmd")          private String[]  cmd;
    @JsonProperty("Dns")          private String[]  dns;
    @JsonProperty("Image")        private String    image;
    @JsonProperty("Volumes")      private Object    volumes;
    @JsonProperty("VolumesFrom")  private String    volumesFrom;
    @JsonProperty("Entrypoint")   private String[]  entrypoint;


    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public void setPortSpecs(String[] portSpecs) {
        this.portSpecs = portSpecs;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isTty() {
        return tty;
    }

    public void setTty(boolean tty) {
        this.tty = tty;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public void setStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public void setStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public void setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public long getMemorySwap() {
        return memorySwap;
    }

    public void setMemorySwap(long memorySwap) {
        this.memorySwap = memorySwap;
    }

    public int getCpuShares() {
        return cpuShares;
    }

    public void setCpuShares(int cpuShares) {
        this.cpuShares = cpuShares;
    }

    public boolean isAttachStdin() {
        return attachStdin;
    }

    public void setAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public void setAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public void setAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
    }

    public String[] getEnv() {
        return env;
    }

    public void setEnv(String[] env) {
        this.env = env;
    }

    public String[] getCmd() {
        return cmd;
    }

    public void setCmd(String[] cmd) {
        this.cmd = cmd;
    }

    public String[] getDns() {
        return dns;
    }

    public void setDns(String[] dns) {
        this.dns = dns;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getVolumes() {
        return volumes;
    }

    public void setVolumes(Object volumes) {
        this.volumes = volumes;
    }

    public String getVolumesFrom() {
        return volumesFrom;
    }

    public void setVolumesFrom(String volumesFrom) {
        this.volumesFrom = volumesFrom;
    }

    public String[] getEntrypoint() {
        return entrypoint;
    }

    public void setEntrypoint(String[] entrypoint) {
        this.entrypoint = entrypoint;
    }

    @Override
    public String toString() {
        return "ContainerConfig{" +
                "hostName='" + hostName + '\'' +
                ", portSpecs=" + Arrays.toString(portSpecs) +
                ", user='" + user + '\'' +
                ", tty=" + tty +
                ", stdinOpen=" + stdinOpen +
                ", stdInOnce=" + stdInOnce +
                ", memoryLimit=" + memoryLimit +
                ", memorySwap=" + memorySwap +
                ", cpuShares=" + cpuShares +
                ", attachStdin=" + attachStdin +
                ", attachStdout=" + attachStdout +
                ", attachStderr=" + attachStderr +
                ", env=" + env +
                ", cmd=" + Arrays.toString(cmd) +
                ", dns=" + Arrays.toString(dns) +
                ", image='" + image + '\'' +
                ", volumes=" + volumes +
                ", volumesFrom='" + volumesFrom + '\'' +
                ", entrypoint='" + Arrays.toString(portSpecs) +
                '}';
    }
}
