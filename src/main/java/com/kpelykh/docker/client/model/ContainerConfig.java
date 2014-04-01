package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Arrays;
import java.util.Map;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ContainerConfig {

    @JsonProperty("Hostname")     private String    hostName = "";
    @JsonProperty("PortSpecs")    private String[]  portSpecs;
    @JsonProperty("User")         private String    user = "";
    @JsonProperty("Tty")          private boolean   tty = false;
    @JsonProperty("OpenStdin")    private boolean   stdinOpen = false;
    @JsonProperty("StdinOnce")    private boolean   stdInOnce = false;
    @JsonProperty("Memory")       private long      memoryLimit = 0;
    @JsonProperty("MemorySwap")   private long      memorySwap = 0;
    @JsonProperty("CpuShares")    private int       cpuShares = 0;
    @JsonProperty("AttachStdin")  private boolean   attachStdin = false;
    @JsonProperty("AttachStdout") private boolean   attachStdout = false;
    @JsonProperty("AttachStderr") private boolean   attachStderr = false;
    @JsonProperty("Env")          private String[]  env;
    @JsonProperty("Cmd")          private String[]  cmd;
    @JsonProperty("Dns")          private String[]  dns;
    @JsonProperty("Image")        private String    image;
    @JsonProperty("Volumes")      private Object    volumes;
    @JsonProperty("VolumesFrom")  private String    volumesFrom = "";
    @JsonProperty("Entrypoint")   private String[]  entrypoint = new String[]{};
    @JsonProperty("NetworkDisabled") private boolean networkDisabled = false;
    @JsonProperty("Privileged")   private boolean privileged = false;
    @JsonProperty("WorkingDir")   private String workingDir = "";
    @JsonProperty("Domainname")   private String domainName = "";
    // FIXME Is this the right type? -BJE
    @JsonProperty("ExposedPorts")   private Map<String, ?> exposedPorts;
    
    @JsonProperty("OnBuild")   private int[] onBuild;

    public Map<String, ?> getExposedPorts() {
        return exposedPorts;
    }

    public boolean isNetworkDisabled() {
        return networkDisabled;
    }

    public String getDomainName() {
        return domainName;
    }

    public String getWorkingDir() { return workingDir; }

    public ContainerConfig setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public ContainerConfig setPrivileged(boolean privileged) {
        this.privileged = privileged;
        return this;
    }

    public String getHostName() {
        return hostName;
    }

    public ContainerConfig setNetworkDisabled(boolean networkDisabled) {
        this.networkDisabled = networkDisabled;
        return this;
    }

    public ContainerConfig setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public ContainerConfig setPortSpecs(String[] portSpecs) {
        this.portSpecs = portSpecs;
        return this;
    }

    public String getUser() {
        return user;
    }

    public ContainerConfig setUser(String user) {
        this.user = user;
        return this;
    }

    public boolean isTty() {
        return tty;
    }

    public ContainerConfig setTty(boolean tty) {
        this.tty = tty;
        return this;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public ContainerConfig setStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
        return this;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public ContainerConfig setStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
        return this;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public ContainerConfig setMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    public long getMemorySwap() {
        return memorySwap;
    }

    public ContainerConfig setMemorySwap(long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }

    public int getCpuShares() {
        return cpuShares;
    }

    public ContainerConfig setCpuShares(int cpuShares) {
        this.cpuShares = cpuShares;
        return this;
    }

    public boolean isAttachStdin() {
        return attachStdin;
    }

    public ContainerConfig setAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public ContainerConfig setAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public ContainerConfig setAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    public String[] getEnv() {
        return env;
    }

    public ContainerConfig setEnv(String[] env) {
        this.env = env;
        return this;
    }

    public String[] getCmd() {
        return cmd;
    }

    public ContainerConfig setCmd(String[] cmd) {
        this.cmd = cmd;
        return this;
    }

    public String[] getDns() {
        return dns;
    }

    public ContainerConfig setDns(String[] dns) {
        this.dns = dns;
        return this;
    }

    public String getImage() {
        return image;
    }

    public ContainerConfig setImage(String image) {
        this.image = image;
        return this;
    }

    public Object getVolumes() {
        return volumes;
    }

    public ContainerConfig setVolumes(Object volumes) {
        this.volumes = volumes;
        return this;
    }

    public String getVolumesFrom() {
        return volumesFrom;
    }

    public ContainerConfig setVolumesFrom(String volumesFrom) {
        this.volumesFrom = volumesFrom;
        return this;
    }

    public String[] getEntrypoint() {
        return entrypoint;
    }

    public ContainerConfig setEntrypoint(String[] entrypoint) {
        this.entrypoint = entrypoint;
        return this;
    }
    
    public void setOnBuild(int[] onBuild) {
		this.onBuild = onBuild;
	}
    
    public int[] getOnBuild() {
		return onBuild;
	}
    
    public void setDomainName(String domainName) {
		this.domainName = domainName;
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
                ", env=" + Arrays.toString(env) +
                ", cmd=" + Arrays.toString(cmd) +
                ", dns=" + Arrays.toString(dns) +
                ", image='" + image + '\'' +
                ", volumes=" + volumes +
                ", volumesFrom='" + volumesFrom + '\'' +
                ", entrypoint=" + Arrays.toString(entrypoint) +
                ", networkDisabled=" + networkDisabled +
                ", privileged=" + privileged +
                ", workingDir='" + workingDir + '\'' +
                ", domainName='" + domainName + '\'' +
                ", onBuild='" + onBuild + '\'' +
                '}';
    }
}
