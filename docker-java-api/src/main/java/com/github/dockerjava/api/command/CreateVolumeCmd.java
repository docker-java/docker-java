package com.github.dockerjava.api.command;

import java.util.Map;

import javax.annotation.CheckForNull;

public interface CreateVolumeCmd extends SyncDockerCmd<CreateVolumeResponse> {

    @CheckForNull
    String getName();

    @CheckForNull
    Map<String, String> getLabels();

    @CheckForNull
    String getDriver();

    @CheckForNull
    Map<String, String> getDriverOpts();

    /**
     * @param name
     *            - The new volume’s name. If not specified, Docker generates a name.
     */
    CreateVolumeCmd withName(String name);

    /**
     * @param labels
     *            - A mapping of labels keys and values. Labels are a mechanism for applying metadata to Docker objects.
     */
    CreateVolumeCmd withLabels(Map<String, String> labels);

    /**
     * @param driver
     *            - Name of the volume driver to use. Defaults to local for the name.
     */
    CreateVolumeCmd withDriver(String driver);

    /**
     * @param driverOpts
     *            - A mapping of driver options and values. These options are passed directly to the driver and are driver specific.
     */
    CreateVolumeCmd withDriverOpts(Map<String, String> driverOpts);

    interface Exec extends DockerCmdSyncExec<CreateVolumeCmd, CreateVolumeResponse> {
    }
}
