package com.github.dockerjava.api.command;

import org.immutables.value.Value;

import javax.annotation.Nullable;

@Value.Immutable
@ImmutableSpec
interface InspectContainer {

    @Value.Parameter
    String getContainerId();

    @Nullable
    Boolean getSize();
}
