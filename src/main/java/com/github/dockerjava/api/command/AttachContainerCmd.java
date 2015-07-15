package com.github.dockerjava.api.command;

import java.io.InputStream;

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

    public String getContainerId();

    public boolean hasLogsEnabled();

    public boolean hasFollowStreamEnabled();

    public boolean hasTimestampsEnabled();

    public boolean hasStdoutEnabled();

    public boolean hasStderrEnabled();

    public AttachContainerCmd withContainerId(String containerId);

    /**
     * See {@link #withFollowStream(boolean)}
     */
    public AttachContainerCmd withFollowStream();

    /**
     * Following the stream means the resulting {@link InputStream} returned by {@link #exec()} reads infinitely. So a
     * {@link InputStream#read()} MAY BLOCK FOREVER as long as no data is streamed from the docker host to
     * {@link DockerClient}!
     */
    public AttachContainerCmd withFollowStream(boolean followStream);

    public AttachContainerCmd withTimestamps(boolean timestamps);

    public AttachContainerCmd withStdOut();

    public AttachContainerCmd withStdOut(boolean stdout);

    public AttachContainerCmd withStdErr();

    public AttachContainerCmd withStdErr(boolean stderr);

    public AttachContainerCmd withLogs(boolean logs);

    public AttachContainerCmd withLogs();

    public static interface Exec extends DockerCmdAsyncExec<AttachContainerCmd, Frame> {
    }

}
