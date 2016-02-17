package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.VersionCmd;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.annotation.CheckForNull;

/**
 * Used for `/version`
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 * @see VersionCmd
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Version {

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
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Version version1 = (Version) o;

        return new EqualsBuilder()
                .append(apiVersion, version1.apiVersion)
                .append(arch, version1.arch)
                .append(gitCommit, version1.gitCommit)
                .append(goVersion, version1.goVersion)
                .append(kernelVersion, version1.kernelVersion)
                .append(operatingSystem, version1.operatingSystem)
                .append(version, version1.version)
                .append(buildTime, version1.buildTime)
                .append(experimental, version1.experimental)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(apiVersion)
                .append(arch)
                .append(gitCommit)
                .append(goVersion)
                .append(kernelVersion)
                .append(operatingSystem)
                .append(version)
                .append(buildTime)
                .append(experimental)
                .toHashCode();
    }
}
