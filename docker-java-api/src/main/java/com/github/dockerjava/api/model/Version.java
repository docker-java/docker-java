package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dockerjava.api.command.VersionCmd;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

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

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_25}
     */
    @JsonProperty("MinAPIVersion")
    private String minAPIVersion;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_35}
     */
    @JsonProperty("Platform")
    private VersionPlatform platform;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_35}
     */
    @JsonProperty("Components")
    private List<VersionComponent> components;

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

    /**
     * @see #minAPIVersion
     */
    @CheckForNull
    public String getMinAPIVersion() {
        return minAPIVersion;
    }

    /**
     * @see #platform
     */
    @CheckForNull
    public VersionPlatform getPlatform() {
        return platform;
    }

    /**
     * @see #components
     */
    @CheckForNull
    public List<VersionComponent> getComponents() {
        return components;
    }

    @Override
    public String toString() {
        return "Version{" +
                "apiVersion='" + apiVersion + '\'' +
                ", arch='" + arch + '\'' +
                ", gitCommit='" + gitCommit + '\'' +
                ", goVersion='" + goVersion + '\'' +
                ", kernelVersion='" + kernelVersion + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", version='" + version + '\'' +
                ", buildTime='" + buildTime + '\'' +
                ", experimental=" + experimental +
                ", minAPIVersion='" + minAPIVersion + '\'' +
                ", platform=" + platform +
                ", components=" + components +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Version version1 = (Version) o;
        return Objects.equals(apiVersion, version1.apiVersion) &&
                Objects.equals(arch, version1.arch) &&
                Objects.equals(gitCommit, version1.gitCommit) &&
                Objects.equals(goVersion, version1.goVersion) &&
                Objects.equals(kernelVersion, version1.kernelVersion) &&
                Objects.equals(operatingSystem, version1.operatingSystem) &&
                Objects.equals(version, version1.version) &&
                Objects.equals(buildTime, version1.buildTime) &&
                Objects.equals(experimental, version1.experimental) &&
                Objects.equals(minAPIVersion, version1.minAPIVersion) &&
                Objects.equals(platform, version1.platform) &&
                Objects.equals(components, version1.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiVersion, arch, gitCommit, goVersion, kernelVersion, operatingSystem, version, buildTime, experimental, minAPIVersion, platform, components);
    }
}
