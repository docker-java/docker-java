package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.PruneCmd;
import com.github.dockerjava.api.model.PruneResponse;
import com.github.dockerjava.api.model.PruneType;
import com.github.dockerjava.core.util.FiltersBuilder;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Delete unused content (containers, images, volumes, networks, build relicts)
 */
public class PruneCmdImpl extends AbstrDockerCmd<PruneCmd, PruneResponse> implements PruneCmd {

    private static final String BUILD_API_PATH = "/build/prune";
    private static final String CONTAINERS_API_PATH = "/containers/prune";
    private static final String IMAGES_API_PATH = "/images/prune";
    private static final String VOLUMES_API_PATH = "/volumes/prune";
    private static final String NETWORKS_API_PATH = "/networks/prune";

    private FiltersBuilder filters = new FiltersBuilder();
    private PruneType pruneType;

    public PruneCmdImpl(Exec exec, PruneType pruneType) {
        super(exec);
        this.pruneType = pruneType;
    }

    @Nonnull
    @Override
    public PruneType getPruneType() {
        return pruneType;
    }

    @Nonnull
    @Override
    public String getApiPath() {
        String apiPath;
        switch (getPruneType()) {
            case BUILD:
                apiPath = BUILD_API_PATH;
                break;
            case IMAGES:
                apiPath = IMAGES_API_PATH;
                break;
            case NETWORKS:
                apiPath = NETWORKS_API_PATH;
                break;
            case VOLUMES:
                apiPath = VOLUMES_API_PATH;
                break;
            default:
                apiPath = CONTAINERS_API_PATH;
                break;
        }
        return apiPath;
    }

    @CheckForNull
    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public PruneCmd withPruneType(final PruneType pruneType) {
        Objects.requireNonNull(pruneType, "pruneType has not been specified");
        this.pruneType = pruneType;
        return this;
    }

    @Override
    public PruneCmd withDangling(Boolean dangling) {
        Objects.requireNonNull(dangling, "dangling has not been specified");
        filters.withFilter("dangling", dangling ? "1" : "0");
        return this;
    }

    @Override
    public PruneCmd withUntilFilter(final String until) {
        Objects.requireNonNull(until, "until has not been specified");
        filters.withUntil(until);
        return this;
    }

    @Override
    public PruneCmd withLabelFilter(final String... labels) {
        Objects.requireNonNull(labels, "labels have not been specified");
        filters.withLabels(labels);
        return this;
    }

    @Override
    public PruneResponse exec() {
        return super.exec();
    }
}
