package com.github.dockerjava.api.command;

public interface DockerCmd<RES_T> {

	public RES_T exec();

}