package com.github.dockerjava.api.command;

import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

@Value.Immutable
@ImmutableSpec
interface ListContainers {

    @Nullable
    String getBeforeId();

    @Nullable
    Map<String, List<String>> getFilters();

    @Nullable
    @Value.Default
    default Integer getLimit() {
        return -1;
    }

    @Nullable
    String getSinceId();

    @Nullable
    Boolean hasShowAllEnabled();

    @Nullable
    Boolean hasShowSizeEnabled();
}
