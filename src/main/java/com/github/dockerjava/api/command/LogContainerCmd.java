package com.github.dockerjava.api.command;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.NotFoundException;
import com.github.dockerjava.api.model.Frame;

import java.io.InputStream;

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
 */
public interface LogContainerCmd extends AsyncDockerCmd<LogContainerCmd, Frame, Void> {

    public String getContainerId();

    public int getTail();

    public boolean hasFollowStreamEnabled();

    public boolean hasTimestampsEnabled();

    public boolean hasStdoutEnabled();

    public boolean hasStderrEnabled();

    public LogContainerCmd withContainerId(String containerId);

    /**
     * See {@link #withFollowStream(boolean)}
     */
    public LogContainerCmd withFollowStream();

    /**
     * Following the stream means the resulting {@link InputStream} returned by {@link #exec()} reads infinitely. So a
     * {@link InputStream#read()} MAY BLOCK FOREVER as long as no data is streamed from the docker host to
     * {@link DockerClient}!
     */
    public LogContainerCmd withFollowStream(boolean followStream);

    public LogContainerCmd withTimestamps();

    public LogContainerCmd withTimestamps(boolean timestamps);

    public LogContainerCmd withStdOut();

    public LogContainerCmd withStdOut(boolean stdout);

    public LogContainerCmd withStdErr();

    public LogContainerCmd withStdErr(boolean stderr);

    public LogContainerCmd withTailAll();

    public LogContainerCmd withTail(int tail);

    /**
     *
     * @throws NotFoundException
     *             No such container
     */
    @Override
    public Void exec();

    public static interface Exec extends DockerCmdExec<LogContainerCmd, Void> {
    }

}
