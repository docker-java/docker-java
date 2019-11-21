package com.github.dockerjava.api.model;

import com.github.dockerjava.api.annotation.FieldName;
import com.github.dockerjava.api.command.VersionCmd;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.annotation.CheckForNull;
import java.io.Serializable;
import java.util.List;

/**
 * Used for `/version`
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 * @see VersionCmd
 */
@EqualsAndHashCode
@ToString
public class Version implements Serializable {
    private static final long serialVersionUID = 1L;

    @FieldName("ApiVersion")
    private String apiVersion;

    @FieldName("Arch")
    private String arch;

    @FieldName("GitCommit")
    private String gitCommit;

    @FieldName("GoVersion")
    private String goVersion;

    @FieldName("KernelVersion")
    private String kernelVersion;

    @FieldName("Os")
    private String operatingSystem;

    @FieldName("Version")
    private String version;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_22}
     */
    @FieldName("BuildTime")
    private String buildTime;

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_20}
     */
    @FieldName("Experimental")
    private Boolean experimental;

    /**
     * @since ~{@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_25}
     */
    @FieldName("MinAPIVersion")
    private String minAPIVersion;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_35}
     */
    @FieldName("Platform")
    private VersionPlatform platform;

    /**
     * @since {@link com.github.dockerjava.core.RemoteApiVersion#VERSION_1_35}
     */
    @FieldName("Components")
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
}
