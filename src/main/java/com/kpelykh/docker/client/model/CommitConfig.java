package com.kpelykh.docker.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */
public class CommitConfig {

    @JsonProperty("container") public String container;
    @JsonProperty("repo") public String repo;
    @JsonProperty("tag") public String tag;
    @JsonProperty("m") public String message;

    //author (eg. “John Hannibal Smith <hannibal@a-team.com>”)
    @JsonProperty("author") public String author;

    //config automatically applied when the image is run. (ex: {“Cmd”: [“cat”, “/world”], “PortSpecs”:[“22”]})
    @JsonProperty("run") public String run;


    public CommitConfig() {}

    public CommitConfig(Builder builder) {
        this.container = builder.container;
        this.repo = builder.repo;
        this.tag = builder.tag;
        this.message = builder.message;
        this.author = builder.author;
        this.run = builder.run;
    }

    public static class Builder {
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
