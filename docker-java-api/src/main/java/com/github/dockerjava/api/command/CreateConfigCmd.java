package com.github.dockerjava.api.command;

import com.github.dockerjava.api.exception.ConflictException;

import javax.annotation.CheckForNull;
import java.util.Map;

/**
 * Command to create a new config
 *
 * @since {@link RemoteApiVersion#VERSION_1_30}
 */
public interface CreateConfigCmd extends SyncDockerCmd<CreateConfigResponse> {

    @CheckForNull
    String getName();

    @CheckForNull
    String getData();

    @CheckForNull
    Map<String, String> getLabels();

    /**
     * @param name
     *            - The new config name.
     */
    CreateConfigCmd withName(String name);

    /**
     * @param data
     *            - The new config data.
     */
    CreateConfigCmd withData(byte[] data);

    /**
     * @param labels
     *            - A mapping of labels keys and values. Labels are a mechanism for applying metadata to Docker objects.
     */
    CreateConfigCmd withLabels(Map<String, String> labels);

    /**
     * @throws ConflictException Named config already exists
     */
    @Override
    CreateConfigResponse exec() throws ConflictException;

    interface Exec extends DockerCmdSyncExec<CreateConfigCmd, CreateConfigResponse> {
    }

}
