package com.github.dockerjava.api.command;


import javax.annotation.CheckForNull;

public interface JoinSwarmCmd extends SyncDockerCmd<Void> {

    @CheckForNull
    String getListenAddr();

    JoinSwarmCmd withListenAddr(String listenAddr);

    @CheckForNull
    String getAdvertiseAddr();

    JoinSwarmCmd withAdvertiseAddr(String advertiseAddr);

    @CheckForNull
    String[] getRemoteAddrs();

    JoinSwarmCmd withRemoteAddrs(String[] remoteAddrs);

    @CheckForNull
    String getJoinToken();

    JoinSwarmCmd withJoinToken(String joinToken);

    @Override
    Void exec();

    interface Exec extends DockerCmdSyncExec<JoinSwarmCmd, Void> {
    }
}
