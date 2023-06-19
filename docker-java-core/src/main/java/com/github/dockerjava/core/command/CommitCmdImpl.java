package com.github.dockerjava.core.command;

import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.CommitCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.ExposedPorts;
import com.github.dockerjava.api.model.Volumes;

/**
 *
 * Create a new image from a container's changes. Returns the new image ID.
 *
 */
public class CommitCmdImpl extends AbstrDockerCmd<CommitCmd, String> implements CommitCmd {

    private String containerId, repository, tag, message, author;

    private Boolean pause = true;

    @JsonProperty("AttachStdin")
    private Boolean attachStdin;

    @JsonProperty("AttachStdout")
    private Boolean attachStdout;

    @JsonProperty("AttachStderr")
    private Boolean attachStderr;

    @JsonProperty("Cmd")
    private String[] cmd;

    @JsonProperty("DisableNetwork")
    private Boolean disableNetwork;

    @JsonProperty("Env")
    private String[] env;

    @JsonProperty("ExposedPorts")
    private ExposedPorts exposedPorts;

    @JsonProperty("Hostname")
    private String hostname;

    /**
     * @since 1.19
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    @JsonProperty("Memory")
    private Integer memory;

    @JsonProperty("MemorySwap")
    private Integer memorySwap;

    @JsonProperty("OpenStdin")
    private Boolean openStdin;

    @JsonProperty("PortSpecs")
    private String[] portSpecs;

    @JsonProperty("StdinOnce")
    private Boolean stdinOnce;

    @JsonProperty("Tty")
    private Boolean tty;

    @JsonProperty("User")
    private String user;

    @JsonProperty("Volumes")
    private Volumes volumes;

    @JsonProperty("WorkingDir")
    private String workingDir;

    public CommitCmdImpl(CommitCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public String getContainerId() {
        return containerId;
    }

    @Override
    public CommitCmdImpl withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public String getRepository() {
        return repository;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public Boolean hasPauseEnabled() {
        return pause;
    }

    @Override
    public CommitCmdImpl withAttachStderr(Boolean attachStderr) {
        this.attachStderr = attachStderr;
        return this;
    }

    @Override
    public CommitCmdImpl withAttachStdin(Boolean attachStdin) {
        this.attachStdin = attachStdin;
        return this;
    }

    @Override
    public CommitCmdImpl withAttachStdout(Boolean attachStdout) {
        this.attachStdout = attachStdout;
        return this;
    }

    @Override
    public CommitCmdImpl withCmd(String... cmd) {
        this.cmd = Objects.requireNonNull(cmd, "cmd was not specified");
        return this;
    }

    @Override
    public CommitCmdImpl withDisableNetwork(Boolean disableNetwork) {
        this.disableNetwork = disableNetwork;
        return this;
    }

    @Override
    public CommitCmdImpl withAuthor(String author) {
        this.author = Objects.requireNonNull(author, "author was not specified");
        return this;
    }

    @Override
    public CommitCmdImpl withMessage(String message) {
        this.message = Objects.requireNonNull(message, "message was not specified");
        return this;
    }

    @Override
    public CommitCmdImpl withTag(String tag) {
        this.tag = Objects.requireNonNull(tag, "tag was not specified");
        return this;
    }

    @Override
    public CommitCmdImpl withRepository(String repository) {
        this.repository = Objects.requireNonNull(repository, "repository was not specified");
        return this;
    }

    @Override
    public CommitCmdImpl withPause(Boolean pause) {
        this.pause = pause;
        return this;
    }

    @Override
    public String[] getEnv() {
        return env;
    }

    @Override
    public CommitCmdImpl withEnv(String... env) {
        this.env = Objects.requireNonNull(env, "env was not specified");
        return this;
    }

    @Override
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public CommitCmdImpl withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    @Override
    public ExposedPorts getExposedPorts() {
        return exposedPorts;
    }

    @Override
    public CommitCmdImpl withExposedPorts(ExposedPorts exposedPorts) {
        this.exposedPorts = Objects.requireNonNull(exposedPorts, "exposedPorts was not specified");
        return this;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public CommitCmdImpl withHostname(String hostname) {
        this.hostname = Objects.requireNonNull(hostname, "hostname was not specified");
        return this;
    }

    @Override
    public Integer getMemory() {
        return memory;
    }

    @Override
    public CommitCmdImpl withMemory(Integer memory) {
        this.memory = Objects.requireNonNull(memory, "memory was not specified");
        return this;
    }

    @Override
    public Integer getMemorySwap() {
        return memorySwap;
    }

    @Override
    public CommitCmdImpl withMemorySwap(Integer memorySwap) {
        this.memorySwap = Objects.requireNonNull(memorySwap, "memorySwap was not specified");
        return this;
    }

    @Override
    public Boolean isOpenStdin() {
        return openStdin;
    }

    @Override
    public CommitCmdImpl withOpenStdin(Boolean openStdin) {
        this.openStdin = Objects.requireNonNull(openStdin, "openStdin was not specified");
        return this;
    }

    @Override
    public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
    public CommitCmdImpl withPortSpecs(String... portSpecs) {
        this.portSpecs = Objects.requireNonNull(portSpecs, "portSpecs was not specified");
        return this;
    }

    @Override
    public Boolean isStdinOnce() {
        return stdinOnce;
    }

    @Override
    public CommitCmdImpl withStdinOnce(Boolean stdinOnce) {
        this.stdinOnce = stdinOnce;
        return this;
    }

    @Override
    public Boolean isTty() {
        return tty;
    }

    @Override
    public CommitCmdImpl withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public CommitCmdImpl withUser(String user) {
        this.user = Objects.requireNonNull(user, "user was not specified");
        return this;
    }

    @Override
    public Volumes getVolumes() {
        return volumes;
    }

    @Override
    public CommitCmdImpl withVolumes(Volumes volumes) {
        this.volumes = Objects.requireNonNull(volumes, "volumes was not specified");
        return this;
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public CommitCmdImpl withWorkingDir(String workingDir) {
        this.workingDir = Objects.requireNonNull(workingDir, "workingDir was not specified");
        return this;
    }

    /**
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public String exec() throws NotFoundException {
        return super.exec();
    }
}
