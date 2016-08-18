package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.VersionCmd;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;
import java.io.Serializable;

/**
 * Used for `/version`
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 * @see VersionCmd
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("ApiVersion")
    private String apiVersion;

    @JsonProperty("Arch")
    private String arch;

    @JsonProperty("GitCommit")
    private String gitCommit;

    @JsonProperty("GoVersion")
    private String goVersion;

    @JsonProperty("KernelVersion")
    private String kernelVersion;

    @JsonProperty("Os")
    private String operatingSystem;

    @JsonProperty("Version")
    private String version;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @JsonProperty("BuildTime")
    private String buildTime;

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @JsonProperty("Experimental")
    private Boolean experimental;

    public String getVersion() {
        return version;
    }

    public String getGitCommit() {
        return gitCommit;
    }

    public String getGoVersion() {
        return goVersion;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public String getArch() {
        return arch;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * @see #buildTime
     */
    @CheckForNull
    public String getBuildTime() {
        return buildTime;
    }

    /**
     * @see #experimental
     */
    @CheckForNull
    public Boolean getExperimental() {
        return experimental;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object o) {
        return  EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
