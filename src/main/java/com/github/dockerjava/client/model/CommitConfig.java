package com.github.dockerjava.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommitConfig {

    @JsonProperty("container")
    private String containerId;

    @JsonProperty("repo")
    private String repo;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("m")
    private String message;

    //author (eg. “John Hannibal Smith <hannibal@a-team.com>”)
    @JsonProperty("author")
    private String author;

    //config automatically applied when the image is run. (ex: {“Cmd”: [“cat”, “/world”], “PortSpecs”:[“22”]})
    @JsonProperty("run")
    private String run;

    public String getContainerId() {
        return containerId;
    }

    public String getRepo() {
        return repo;
    }

    public String getTag() {
        return tag;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getRun() {
        return run;
    }

    public CommitConfig setRepo(String repo) {
        this.repo = repo;
        return this;
    }

    public CommitConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public CommitConfig setMessage(String message) {
        this.message = message;
        return this;
    }

    public CommitConfig setAuthor(String author) {
        this.author = author;
        return this;
    }

    public CommitConfig setRun(String run) {
        this.run = run;
        return this;
    }

    public CommitConfig(String containerId) {
        this.containerId = containerId;
    }
    
}
