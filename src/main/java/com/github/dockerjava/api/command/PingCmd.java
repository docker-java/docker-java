package com.github.dockerjava.api.command;

/**
 * Ping the Docker server
 * 
 */
public interface PingCmd extends DockerCmd<Void> {

    public static interface Exec extends DockerCmdExec<PingCmd, Void> {
    }

}