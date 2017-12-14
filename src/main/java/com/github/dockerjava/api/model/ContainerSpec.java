package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.core.RemoteApiVersion;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The specification for containers as used in {@link TaskSpec}
 *
 * @since {@link RemoteApiVersion#VERSION_1_24}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerSpec implements Serializable {
    public static final Long serialVersionUID = 1L;

    /**
     * @since 1.24
     */
    @JsonProperty("Image")
    private String image;

    /**
     * @since 1.24
     */
    @JsonProperty("Labels")
    private Map<String, String> labels;

    /**
     * @since 1.24
     */
    @JsonProperty("Command")
    private List<String> command;

    /**
     * @since 1.24
     */
    @JsonProperty("Args")
    private List<String> args;

    /**
     * @since 1.24
     */
    @JsonProperty("Env")
    private List<String> env;

    /**
     * @since 1.24
     */
    @JsonProperty("Dir")
    private String dir;

    /**
     * @since 1.24
     */
    @JsonProperty("User")
    private String user;

    /**
     * @since 1.24
     */
    @JsonProperty("Groups")
    private String groups;

    /**
     * @since 1.24
     */
    @JsonProperty("TTY")
    private Boolean tty;

    /**
     * @since 1.24
     */
    @JsonProperty("Mounts")
    private List<Mount> mounts;

    /**
     * @since 1.24
     */
    @JsonProperty("Duration")
    private Long duration;

    /**
     * @see #image
     */
    @CheckForNull
    public String getImage() {
        return image;
    }

    /**
     * @see #image
     */
    public ContainerSpec withImage(String image) {
        this.image = image;
        return this;
    }

    /**
     * @see #labels
     */
    @CheckForNull
    public Map<String, String> getLabels() {
        return labels;
    }

    /**
     * @see #labels
     */
    public ContainerSpec withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    /**
     * @see #command
     */
    @CheckForNull
    public List<String> getCommand() {
        return command;
    }

    /**
     * @see #command
     */
    public ContainerSpec withCommand(List<String> command) {
        this.command = command;
        return this;
    }

    /**
     * @see #args
     */
    @CheckForNull
    public List<String> getArgs() {
        return args;
    }

    /**
     * @see #args
     */
    public ContainerSpec withArgs(List<String> args) {
        this.args = args;
        return this;
    }

    /**
     * @see #env
     */
    @CheckForNull
    public List<String> getEnv() {
        return env;
    }

    /**
     * @see #env
     */
    public ContainerSpec withEnv(List<String> env) {
        this.env = env;
        return this;
    }

    /**
     * @see #dir
     */
    @CheckForNull
    public String getDir() {
        return dir;
    }

    /**
     * @see #dir
     */
    public ContainerSpec withDir(String dir) {
        this.dir = dir;
        return this;
    }

    /**
     * @see #user
     */
    @CheckForNull
    public String getUser() {
        return user;
    }

    /**
     * @see #user
     */
    public ContainerSpec withUser(String user) {
        this.user = user;
        return this;
    }

    /**
     * @see #groups
     */
    @CheckForNull
    public String getGroups() {
        return groups;
    }

    /**
     * @see #groups
     */
    public ContainerSpec withGroups(String groups) {
        this.groups = groups;
        return this;
    }

    /**
     * @see #tty
     */
    @CheckForNull
    public Boolean getTty() {
        return tty;
    }

    /**
     * @see #tty
     */
    public ContainerSpec withTty(Boolean tty) {
        this.tty = tty;
        return this;
    }

    /**
     * @see #mounts
     */
    @CheckForNull
    public List<Mount> getMounts() {
        return mounts;
    }

    /**
     * @see #mounts
     */
    public ContainerSpec withMounts(List<Mount> mounts) {
        this.mounts = mounts;
        return this;
    }

    /**
     * @see #duration
     */
    @CheckForNull
    public Long getDuration() {
        return duration;
    }

    /**
     * @see #duration
     */
    public ContainerSpec withDuration(Long duration) {
        this.duration = duration;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

}
