package com.github.dockerjava.client.model;


import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 * 
 * 		"Hostname":"",
         "User":"",
         "Memory":0,
         "MemorySwap":0,
         "AttachStdin":false,
         "AttachStdout":true,
         "AttachStderr":true,
         "PortSpecs":null,
         "Tty":false,
         "OpenStdin":false,
         "StdinOnce":false,
         "Env":null,
         "Cmd":[
                 "date"
         ],
         "Dns":null,
         "Image":"base",
         "Volumes":{
                 "/tmp": {}
         },
         "VolumesFrom":"",
         "WorkingDir":"",
         "DisableNetwork": false,
         "ExposedPorts":{
                 "22/tcp": {}
         }
 * 
 *
 */
public class CreateContainerConfig {

	@JsonProperty("Hostname")     private String    hostName = "";
	@JsonProperty("User")         private String    user = "";
	@JsonProperty("Memory")       private long      memoryLimit = 0;
	@JsonProperty("MemorySwap")   private long      memorySwap = 0;
	@JsonProperty("AttachStdin")  private boolean   attachStdin = false;
	@JsonProperty("AttachStdout") private boolean   attachStdout = false;
	@JsonProperty("AttachStderr") private boolean   attachStderr = false;
    @JsonProperty("PortSpecs")    private String[]  portSpecs;
    @JsonProperty("Tty")          private boolean   tty = false;
    @JsonProperty("OpenStdin")    private boolean   stdinOpen = false;
    @JsonProperty("StdinOnce")    private boolean   stdInOnce = false;
    @JsonProperty("Env")          private String[]  env;
    @JsonProperty("Cmd")          private String[]  cmd;
    @JsonProperty("Dns")          private String[]  dns;
    @JsonProperty("Image")        private String    image;
    @JsonProperty("Volumes")      private Map<String, ?>   volumes;
    @JsonProperty("VolumesFrom")  private String    volumesFrom = "";
    @JsonProperty("WorkingDir")   private String workingDir = "";
    @JsonProperty("DisableNetwork") private boolean disableNetwork = false;
    @JsonProperty("ExposedPorts")   private ExposedPorts exposedPorts;
    
    public CreateContainerConfig withExposedPorts(ExposedPort[] exposedPorts) {
        this.exposedPorts = new ExposedPorts(exposedPorts);
        return this;
    }
    
    @JsonIgnore
    public ExposedPort[] getExposedPorts() {
        return exposedPorts.getExposedPorts();
    }


    public boolean isDisableNetwork() {
        return disableNetwork;
    }

    public String getWorkingDir() { return workingDir; }

    public CreateContainerConfig withWorkingDir(String workingDir) {
        this.workingDir = workingDir;
        return this;
    }

    
    public String getHostName() {
        return hostName;
    }

    public CreateContainerConfig withDisableNetwork(boolean disableNetwork) {
        this.disableNetwork = disableNetwork;
        return this;
    }

    public CreateContainerConfig withHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String[] getPortSpecs() {
        return portSpecs;
    }

    public CreateContainerConfig withPortSpecs(String[] portSpecs) {
        this.portSpecs = portSpecs;
        return this;
    }

    public String getUser() {
        return user;
    }

    public CreateContainerConfig withUser(String user) {
        this.user = user;
        return this;
    }

    public boolean isTty() {
        return tty;
    }

    public CreateContainerConfig withTty(boolean tty) {
        this.tty = tty;
        return this;
    }

    public boolean isStdinOpen() {
        return stdinOpen;
    }

    public CreateContainerConfig withStdinOpen(boolean stdinOpen) {
        this.stdinOpen = stdinOpen;
        return this;
    }

    public boolean isStdInOnce() {
        return stdInOnce;
    }

    public CreateContainerConfig withStdInOnce(boolean stdInOnce) {
        this.stdInOnce = stdInOnce;
        return this;
    }

    public long getMemoryLimit() {
        return memoryLimit;
    }

    public CreateContainerConfig withMemoryLimit(long memoryLimit) {
        this.memoryLimit = memoryLimit;
        return this;
    }

    public long getMemorySwap() {
        return memorySwap;
    }

    public CreateContainerConfig withMemorySwap(long memorySwap) {
        this.memorySwap = memorySwap;
        return this;
    }


    public boolean isAttachStdin() {
        return attachStdin;
    }

    public CreateContainerConfig withAttachStdin(boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    public boolean isAttachStdout() {
        return attachStdout;
    }

    public CreateContainerConfig withAttachStdout(boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    public boolean isAttachStderr() {
        return attachStderr;
    }

    public CreateContainerConfig withAttachStderr(boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    public String[] getEnv() {
        return env;
    }

    public CreateContainerConfig withEnv(String[] env) {
        this.env = env;
        return this;
    }

    public String[] getCmd() {
        return cmd;
    }

    public CreateContainerConfig withCmd(String[] cmd) {
        this.cmd = cmd;
        return this;
    }

    public String[] getDns() {
        return dns;
    }

    public CreateContainerConfig withDns(String[] dns) {
        this.dns = dns;
        return this;
    }

    public String getImage() {
        return image;
    }

    public CreateContainerConfig withImage(String image) {
        this.image = image;
        return this;
    }

    public Map<String, ?> getVolumes() {
        return volumes;
    }

    public CreateContainerConfig withVolumes(Map<String, ?> volumes) {
        this.volumes = volumes;
        return this;
    }

    public String getVolumesFrom() {
        return volumesFrom;
    }

    public CreateContainerConfig withVolumesFrom(String volumesFrom) {
        this.volumesFrom = volumesFrom;
        return this;
    }

    @Override
    public String toString() {
        return "CreateContainerConfig{" +
                "hostName='" + hostName + '\'' +
                ", portSpecs=" + Arrays.toString(portSpecs) +
                ", user='" + user + '\'' +
                ", tty=" + tty +
                ", stdinOpen=" + stdinOpen +
                ", stdInOnce=" + stdInOnce +
                ", memoryLimit=" + memoryLimit +
                ", memorySwap=" + memorySwap +
                ", attachStdin=" + attachStdin +
                ", attachStdout=" + attachStdout +
                ", attachStderr=" + attachStderr +
                ", env=" + Arrays.toString(env) +
                ", cmd=" + Arrays.toString(cmd) +
                ", dns=" + Arrays.toString(dns) +
                ", image='" + image + '\'' +
                ", volumes=" + volumes +
                ", volumesFrom='" + volumesFrom + '\'' +
                ", disableNetwork=" + disableNetwork +
                ", workingDir='" + workingDir + '\'' +
                '}';
    }
    
    
}
