package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.command.EventStreamReader;

import java.io.InputStream;

/**
 *
 * Pull image from repository.
 *
 */
public interface PullImageCmd extends DockerCmd<InputStream> {

    public String getRepository();

    public String getTag();

    public String getRegistry();

    public AuthConfig getAuthConfig();

    public PullImageCmd withRepository(String repository);

    public PullImageCmd withTag(String tag);

    public PullImageCmd withRegistry(String registry);

    public PullImageCmd withAuthConfig(AuthConfig authConfig);

    public static interface Exec extends DockerCmdExec<PullImageCmd, InputStream> {
    }

    /**
     * Its the responsibility of the caller to consume and/or close the {@link InputStream} to prevent connection leaks.
     *
     * @see {@link EventStreamReader}
     */
    @Override
    public InputStream exec();

}