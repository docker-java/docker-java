package com.github.dockerjava.client.command;

public interface DockerCmd<RES_T> {

	public RES_T exec();

}