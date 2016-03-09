package com.github.dockerjava.api.command;

import java.io.InputStream;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;

/**
 * Get container logs
 *
 * @param followStream
 *            - true or false, return stream. Defaults to false.
 * @param stdout
 *            - true or false, includes stdout log. Defaults to false.
 * @param stderr
 *            - true or false, includes stderr log. Defaults to false.
 * @param timestamps
 *            - true or false, if true, print timestamps for every log line. Defaults to false.
 * @param tail
 *            - `all` or `<number>`, Output specified number of lines at the end of logs
 * @param since
 *            - UNIX timestamp (integer) to filter logs. Specifying a timestamp will only output log-entries since that timestamp. Default:
 *            0 (unfiltered)
 */
public interface LogContainerCmd extends AsyncDockerCmd<LogContainerCmd, Frame> {

    @CheckForNull
    String getContainerId();

    @CheckForNull
    Integer getTail();

    @CheckForNull
    Boolean hasFollowStreamEnabled();

    @CheckForNull
    Boolean hasTimestampsEnabled();

    @CheckForNull
    Boolean hasStdoutEnabled();

    @CheckForNull
    Boolean hasStderrEnabled();

    @CheckForNull
    Integer getSince();

    LogContainerCmd withContainerId(@Nonnull String containerId);

    /**
     * Following the stream means the resulting {@link InputStream} returned by {@link #exec()} reads infinitely. So a
     * {@link InputStream#read()} MAY BLOCK FOREVER as long as no data is streamed from the docker host to {@link DockerClient}!
     */
    LogContainerCmd withFollowStream(Boolean followStream);

    LogContainerCmd withTimestamps(Boolean timestamps);

    LogContainerCmd withStdOut(Boolean stdout);

    LogContainerCmd withStdErr(Boolean stderr);

    LogContainerCmd withTailAll();

    LogContainerCmd withTail(Integer tail);

    LogContainerCmd withSince(Integer since);

    /**
     * @throws com.github.dockerjava.api.NotFoundException
     *             No such container
     */
    @Override
    <T extends ResultCallback<Frame>> T exec(T resultCallback);

    interface Exec extends DockerCmdAsyncExec<LogContainerCmd, Frame> {
    }

}
