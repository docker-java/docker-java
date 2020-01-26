package com.github.dockerjava.api.command;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@ImmutableSpec
interface StopContainer {

    @Value.Parameter
    String getContainerId();

    @Nullable
    default Integer getTimeout() {
        return 10;
    }
}
