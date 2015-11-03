package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
