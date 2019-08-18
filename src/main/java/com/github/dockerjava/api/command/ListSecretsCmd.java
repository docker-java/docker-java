package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Secret;

import javax.annotation.CheckForNull;
import java.util.List;
import java.util.Map;

/**
 * List secrets
 */
public interface ListSecretsCmd extends SyncDockerCmd<List<Secret>> {

    @CheckForNull
    Map<String, List<String>> getFilters();

    /**
     * @param ids - Show only secrets with the given ids
     */
    ListSecretsCmd withIdFilter(List<String> ids);

    /**
     * @param names - Show only secrets with the given names
     */
    ListSecretsCmd withNameFilter(List<String> names);

    /**
     * @param labels - Show only secrets with the passed labels. Labels is a {@link Map} that contains label keys and values
     */
    ListSecretsCmd withLabelFilter(Map<String, String> labels);

    interface Exec extends DockerCmdSyncExec<ListSecretsCmd, List<Secret>> {
    }
}
