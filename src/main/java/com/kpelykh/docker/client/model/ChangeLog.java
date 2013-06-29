package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class ChangeLog {

    @JsonProperty("Path")
    public String path;

    @JsonProperty("Kind")
    public int kind;

    @Override
    public String toString() {
        return "ChangeLog{" +
                "path='" + path + '\'' +
                ", kind=" + kind +
                '}';
    }
}
