package com.github.dockerjava.api.command;

public interface DockerCmd<RES_T> extends AutoCloseable {

    @Override
    public void close();

}