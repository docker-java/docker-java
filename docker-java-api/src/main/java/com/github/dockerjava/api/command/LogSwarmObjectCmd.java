package com.github.dockerjava.api.command;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Frame;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Get docker service/task stdout/stderr logs
 */
public interface LogSwarmObjectCmd extends AsyncDockerCmd<LogSwarmObjectCmd, Frame> {
    /**
     * @param id ID or name of the service
     * @return self
     */
    LogSwarmObjectCmd withId(@Nonnull String id);

    /**
     * @param follow Return the logs as a raw stream.
     * @return self
     */
    LogSwarmObjectCmd withFollow(Boolean follow);

    /**
     * @param timestamps Add timestamps to every log line
     * @return self
     */
    LogSwarmObjectCmd withTimestamps(Boolean timestamps);

    /**
     * @param stdout Return logs from stdout
     * @return self
     */
    LogSwarmObjectCmd withStdout(Boolean stdout);

    /**
     * @param stderr Return logs from stderr
     * @return self
     */
    LogSwarmObjectCmd withStderr(Boolean stderr);

    /**
     * @param tail only return this number of log lines from the end of the logs.
     * @return self
     */
    LogSwarmObjectCmd withTail(Integer tail);

    /**
     * @param since Only return logs since this time, as a UNIX timestamp
     * @return self
     */
    LogSwarmObjectCmd withSince(Integer since);

    /**
     * @param details Show service context and extra details provided to logs.
     * @return
     */
    LogSwarmObjectCmd withDetails(Boolean details);

    @CheckForNull
    String getId();

    @CheckForNull
    Integer getTail();

    @CheckForNull
    Boolean getFollow();

    @CheckForNull
    Boolean getTimestamps();

    @CheckForNull
    Boolean getStdout();

    @CheckForNull
    Boolean getStderr();

    @CheckForNull
    Integer getSince();

    @CheckForNull
    Boolean getDetails();

    /**
     * @throws com.github.dockerjava.api.exception.NotFoundException no such service
     */
    @Override
    <T extends ResultCallback<Frame>> T exec(T resultCallback);

    interface Exec extends DockerCmdAsyncExec<LogSwarmObjectCmd, Frame> {
    }

}
