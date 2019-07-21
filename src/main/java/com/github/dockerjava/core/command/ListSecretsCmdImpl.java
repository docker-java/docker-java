package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.ListSecretsCmd;
import com.github.dockerjava.api.model.Secret;
import com.github.dockerjava.api.model.Service;
import com.github.dockerjava.core.util.FiltersBuilder;

import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * List services.
 */
public class ListSecretsCmdImpl extends AbstrDockerCmd<ListSecretsCmd, List<Secret>> implements
        ListSecretsCmd {

    private FiltersBuilder filters = new FiltersBuilder();

    public ListSecretsCmdImpl(Exec exec) {
        super(exec);
    }

    @Override
    public Map<String, List<String>> getFilters() {
        return filters.build();
    }

    @Override
    public ListSecretsCmd withIdFilter(List<String> ids) {
        checkNotNull(ids, "ids was not specified");
        this.filters.withFilter("id", ids);
        return this;
    }

    @Override
    public ListSecretsCmd withNameFilter(List<String> names) {
        checkNotNull(names, "names was not specified");
        this.filters.withFilter("name", names);
        return this;
    }

    @Override
    public ListSecretsCmd withLabelFilter(Map<String, String> labels) {
        checkNotNull(labels, "labels was not specified");
        this.filters.withLabels(labels);
        return this;
    }
}
