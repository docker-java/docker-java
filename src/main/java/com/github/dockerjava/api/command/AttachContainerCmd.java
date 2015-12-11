package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Frame;

/**
 * Attach to container
 *
 * @param logs
 *            - true or false, includes logs. Defaults to false.
 *
 * @param followStream
 *            - true or false, return stream. Defaults to false.
 * @param stdout
 *            - true or false, includes stdout log. Defaults to false.
 * @param stderr
 *            - true or false, includes stderr log. Defaults to false.
 * @param timestamps
 *            - true or false, if true, print timestamps for every log line. Defaults to false.
 */
public interface AttachContainerCmd extends AsyncDockerCmd<AttachContainerCmd, Frame> {

    @CheckForNull
    public String getContainerId();

    @CheckForNull
    public Boolean hasLogsEnabled();

    @CheckForNull
    public Boolean hasFollowStreamEnabled();

    @CheckForNull
    public Boolean hasTimestampsEnabled();

    @CheckForNull
    public Boolean hasStdoutEnabled();

    @CheckForNull
    public Boolean hasStderrEnabled();

    @CheckForNull
    public InputStream getStdin();

    public AttachContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * Following the stream means the resulting {@link InputStream} returned by {@link #exec()} reads infinitely. So a
     * {@link InputStream#read()} MAY BLOCK FOREVER as long as no data is streamed from the docker host to
     * {@link DockerClient}!
     */
    public AttachContainerCmd withFollowStream(Boolean followStream);

    public AttachContainerCmd withTimestamps(Boolean timestamps);

    public AttachContainerCmd withStdOut(Boolean stdout);

    public AttachContainerCmd withStdErr(Boolean stderr);

    public AttachContainerCmd withStdIn(InputStream stdin);

    public AttachContainerCmd withLogs(Boolean logs);

    public static interface Exec extends DockerCmdAsyncExec<AttachContainerCmd, Frame> {
    }

}
