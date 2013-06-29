package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class Version {


    @JsonProperty("Version")
    public String version;

    @JsonProperty("GitCommit")
    public String  gitCommit;

    @JsonProperty("GoVersion")
    public String  goVersion;


    @Override
    public String toString() {
        return "Version{" +
                "version='" + version + '\'' +
                ", gitCommit='" + gitCommit + '\'' +
                ", goVersion='" + goVersion + '\'' +
                '}';
    }

}
