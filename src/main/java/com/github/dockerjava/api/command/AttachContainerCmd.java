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

    public Boolean hasLogsEnabled();

    public Boolean hasFollowStreamEnabled();

    public Boolean hasTimestampsEnabled();

    public Boolean hasStdoutEnabled();

    public Boolean hasStderrEnabled();

    public AttachContainerCmd withContainerId(String containerId);

    /**
     * See {@link #withFollowStream(Boolean)}
     */
    public AttachContainerCmd withFollowStream();

    /**
     * Following the stream means the resulting {@link InputStream} returned by {@link #exec()} reads infinitely. So a
     * {@link InputStream#read()} MAY BLOCK FOREVER as long as no data is streamed from the docker host to
     * {@link DockerClient}!
     */
    public AttachContainerCmd withFollowStream(Boolean followStream);

    public AttachContainerCmd withTimestamps(Boolean timestamps);

    public AttachContainerCmd withStdOut();

    public AttachContainerCmd withStdOut(Boolean stdout);

    public AttachContainerCmd withStdErr();

    public AttachContainerCmd withStdErr(Boolean stderr);

    public AttachContainerCmd withLogs(Boolean logs);

    public AttachContainerCmd withLogs();

    public static interface Exec extends DockerCmdAsyncExec<AttachContainerCmd, Frame> {
    }

}
