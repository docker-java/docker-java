package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class CommitConfig {

    @JsonProperty("container")
    private String container;

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

    public String getContainer() {
        return container;
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

    private CommitConfig(Builder b) {
        this.container = b.container;
        this.repo = b.repo;
        this.tag = b.tag;
        this.message = b.message;
        this.author = b.author;
        this.run = b.run;
    }

    public static class Builder implements IBuilder<CommitConfig> {
        private String container;
        private String repo;
        private String tag;
        private String message;
        private String author;
        private String run;

        public Builder(String containerId) {
            this.container = containerId;
        }

        public Builder repo(String repo) {
            this.repo = repo;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder run(String run) {
            this.run = run;
            return this;
        }

        public CommitConfig build() {
            return new CommitConfig(this);
        }
    }

}
