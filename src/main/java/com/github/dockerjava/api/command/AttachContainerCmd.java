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
    String getContainerId();

    @CheckForNull
    Boolean hasLogsEnabled();

    @CheckForNull
    Boolean hasFollowStreamEnabled();

    @CheckForNull
    Boolean hasTimestampsEnabled();

    @CheckForNull
    Boolean hasStdoutEnabled();

    @CheckForNull
    Boolean hasStderrEnabled();

    @CheckForNull
    InputStream getStdin();

    AttachContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * Following the stream means the resulting {@link InputStream} returned by {@link #exec()} reads infinitely. So a
     * {@link InputStream#read()} MAY BLOCK FOREVER as long as no data is streamed from the docker host to {@link DockerClient}!
     */
    AttachContainerCmd withFollowStream(Boolean followStream);

    AttachContainerCmd withTimestamps(Boolean timestamps);

    AttachContainerCmd withStdOut(Boolean stdout);

    AttachContainerCmd withStdErr(Boolean stderr);

    AttachContainerCmd withStdIn(InputStream stdin);

    AttachContainerCmd withLogs(Boolean logs);

    interface Exec extends DockerCmdAsyncExec<AttachContainerCmd, Frame> {
    }

}
