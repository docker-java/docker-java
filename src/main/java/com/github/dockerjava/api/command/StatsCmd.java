package com.github.dockerjava.api.command;

import com.github.dockerjava.api.model.Stats;

/**
 * @author Heng WU(wuheng09@otcaix.iscas.ac.cn)
 *
 */
public interface StatsCmd extends DockerCmd<Stats> {

	public static interface Exec extends DockerCmdExec<StatsCmd, Stats> {
	}
	
	public String getContainerId();
}
