package com.github.dockerjava.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UpdateFailureAction {

    @JsonProperty("pause")
    PAUSE,

    @JsonProperty("continue")
    CONTINUE

}
