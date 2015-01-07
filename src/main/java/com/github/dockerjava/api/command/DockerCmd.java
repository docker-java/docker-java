package com.github.dockerjava.api.command;

import java.io.Closeable;

public interface DockerCmd<RES_T> extends Closeable {

	public RES_T exec();

}