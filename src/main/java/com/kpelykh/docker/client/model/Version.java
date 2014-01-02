package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Version {


    @JsonProperty("Version")
    private String version;

    @JsonProperty("GitCommit")
    private String  gitCommit;

    @JsonProperty("GoVersion")
    private String  goVersion;

    public String getVersion() {
        return version;
    }

    public String getGitCommit() {
        return gitCommit;
    }

    public String getGoVersion() {
        return goVersion;
    }

    @Override
    public String toString() {
        return "Version{" +
                "version='" + version + '\'' +
                ", gitCommit='" + gitCommit + '\'' +
                ", goVersion='" + goVersion + '\'' +
                '}';
    }

}
