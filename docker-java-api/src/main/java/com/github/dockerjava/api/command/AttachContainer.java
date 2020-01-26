package com.github.dockerjava.api.command;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.io.InputStream;

@Value.Immutable
@ImmutableSpec
interface AttachContainer {

    String getContainerId();

    @Nullable
    Boolean hasLogsEnabled();

    @Nullable
    Boolean hasFollowStreamEnabled();

    @Nullable
    Boolean hasTimestampsEnabled();

    @Nullable
    Boolean hasStdoutEnabled();

    @Nullable
    Boolean hasStderrEnabled();

    @Nullable
    InputStream getStdin();
}
