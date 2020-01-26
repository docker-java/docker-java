package com.github.dockerjava.api.command;

import org.immutables.value.Value;

@Value.Immutable
@ImmutableSpec
interface StartContainer {

    @Value.Parameter
    String getContainerId();
}
