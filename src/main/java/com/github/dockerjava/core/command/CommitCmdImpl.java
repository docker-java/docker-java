package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

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
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
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
        checkNotNull(cmd, "cmd was not specified");
        this.cmd = cmd;
        return this;
    }

    @Override
    public CommitCmdImpl withDisableNetwork(Boolean disableNetwork) {
        this.disableNetwork = disableNetwork;
        return this;
    }

    @Override
    public CommitCmdImpl withAuthor(String author) {
        checkNotNull(author, "author was not specified");
        this.author = author;
        return this;
    }

    @Override
    public CommitCmdImpl withMessage(String message) {
        checkNotNull(message, "message was not specified");
        this.message = message;
        return this;
    }

    @Override
    public CommitCmdImpl withTag(String tag) {
        checkNotNull(tag, "tag was not specified");
        this.tag = tag;
        return this;
    }

    @Override
    public CommitCmdImpl withRepository(String repository) {
        checkNotNull(repository, "repository was not specified");
        this.repository = repository;
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
        checkNotNull(env, "env was not specified");
        this.env = env;
        return this;
    }

    @Override
    public ExposedPorts getExposedPorts() {
        return exposedPorts;
    }

    @Override
    public CommitCmdImpl withExposedPorts(ExposedPorts exposedPorts) {
        checkNotNull(exposedPorts, "exposedPorts was not specified");
        this.exposedPorts = exposedPorts;
        return this;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public CommitCmdImpl withHostname(String hostname) {
        checkNotNull(hostname, "hostname was not specified");
        this.hostname = hostname;
        return this;
    }

    @Override
    public Integer getMemory() {
        return memory;
    }

    @Override
    public CommitCmdImpl withMemory(Integer memory) {
        checkNotNull(memory, "memory was not specified");
        this.memory = memory;
        return this;
    }

    @Override
    public Integer getMemorySwap() {
        return memorySwap;
    }

    @Override
    public CommitCmdImpl withMemorySwap(Integer memorySwap) {
        checkNotNull(memorySwap, "memorySwap was not specified");
        this.memorySwap = memorySwap;
        return this;
    }

    @Override
    public Boolean isOpenStdin() {
        return openStdin;
    }

    @Override
    public CommitCmdImpl withOpenStdin(Boolean openStdin) {
        checkNotNull(openStdin, "openStdin was not specified");
        this.openStdin = openStdin;
        return this;
    }

    @Override
    public String[] getPortSpecs() {
        return portSpecs;
    }

    @Override
    public CommitCmdImpl withPortSpecs(String... portSpecs) {
        checkNotNull(portSpecs, "portSpecs was not specified");
        this.portSpecs = portSpecs;
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
        checkNotNull(user, "user was not specified");
        this.user = user;
        return this;
    }

    @Override
    public Volumes getVolumes() {
        return volumes;
    }

    @Override
    public CommitCmdImpl withVolumes(Volumes volumes) {
        checkNotNull(volumes, "volumes was not specified");
        this.volumes = volumes;
        return this;
    }

    @Override
    public String getWorkingDir() {
        return workingDir;
    }

    @Override
    public CommitCmdImpl withWorkingDir(String workingDir) {
        checkNotNull(workingDir, "workingDir was not specified");
        this.workingDir = workingDir;
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
