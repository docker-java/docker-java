package com.github.dockerjava.api.command;

import java.util.Map;

import javax.annotation.CheckForNull;

public interface CreateVolumeCmd extends SyncDockerCmd<CreateVolumeResponse> {

    @CheckForNull
    public String getName();

    @CheckForNull
    public String getDriver();

    @CheckForNull
    public Map<String, String> getDriverOpts();

    /**
     * @param name
     *            - The new volume’s name. If not specified, Docker generates a name.
     */
    public CreateVolumeCmd withName(String name);

    /**
     * @param driver
     *            - Name of the volume driver to use. Defaults to local for the name.
     */
    public CreateVolumeCmd withDriver(String driver);

    /**
     * @param driverOpts
     *            - A mapping of driver options and values. These options are passed directly to the driver and are driver specific.
     */
    public CreateVolumeCmd withDriverOpts(Map<String, String> driverOpts);

    public static interface Exec extends DockerCmdSyncExec<CreateVolumeCmd, CreateVolumeResponse> {
    }
}
