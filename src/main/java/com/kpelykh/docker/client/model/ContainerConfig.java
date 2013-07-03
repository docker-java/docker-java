package com.kpelykh.docker.client.model;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ContainerConfig {

    @JsonProperty("Hostname")     private String hostName;
    @JsonProperty("PortSpecs")    private String[] portSpecs;
    @JsonProperty("User")         private String user;
    @JsonProperty("Tty")          private boolean tty;
    @JsonProperty("OpenStdin")    private boolean stdinOpen;
    @JsonProperty("StdinOnce")    private boolean stdInOnce;
    @JsonProperty("Memory")       private int    memoryLimit;
    @JsonProperty("MemorySwap")   private int    memorySwap;
    @JsonProperty("CpuShares")    private int    cpuShares;
    @JsonProperty("AttachStdin")  private boolean attachStdin;
    @JsonProperty("AttachStdout") private boolean attachStdout;
    @JsonProperty("AttachStderr") private boolean attachStderr;
    @JsonProperty("Env")          private Map<String, String> env;
    @JsonProperty("Cmd")          private String[]  cmd;
    @JsonProperty("Dns")          private String[]  dns;
    @JsonProperty("Image")        private String  image;
    @JsonProperty("Volumes")      private Object  volumes;
    @JsonProperty("VolumesFrom")  private String  volumesFrom;

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setPortSpecs(String[] portSpecs) {
        this.portSpecs = portSpecs;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setTty(boolean tty) {
        this.tty = tty;
    }

    public void setStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
    }

    public void setStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
    }

    public void setMemoryLimit(int memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    public void setMemorySwap(int memorySwap) {
        this.memorySwap = memorySwap;
    }

    public void setCpuShares(int cpuShares) {
        this.cpuShares = cpuShares;
    }

    public void setAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
    }

    public void setAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
    }

    public void setAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
    }

    public void setEnv(Map<String, String> env) {
        this.env = env;
    }

    public void setCmd(String[] cmd) {
        this.cmd = cmd;
    }

    public void setDns(String[] dns) {
        this.dns = dns;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVolumes(Object volumes) {
        this.volumes = volumes;
    }

    public void setVolumesFrom(String volumesFrom) {
        this.volumesFrom = volumesFrom;
    }

    public String getHostName() {
        return hostName;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public String getUser() {
        return user;
    }

    public boolean isTty() {
        return tty;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public int getMemorySwap() {
        return memorySwap;
    }

    public int getCpuShares() {
        return cpuShares;
    }

    public boolean isAttachStdin() {
        return attachStdin;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public Map<String, String> getEnv() {
        return env;
    }

    public String[] getCmd() {
        return cmd;
    }

    public String[] getDns() {
        return dns;
    }

    public String getImage() {
        return image;
    }

    public Object getVolumes() {
        return volumes;
    }

    public String getVolumesFrom() {
        return volumesFrom;
    }
}
