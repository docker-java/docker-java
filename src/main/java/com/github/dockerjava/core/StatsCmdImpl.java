package com.github.dockerjava.core;

import static com.google.common.base.Preconditions.checkNotNull;

import com.github.dockerjava.api.command.StatsCmd;
import com.github.dockerjava.api.model.Stats;
import com.github.dockerjava.core.command.AbstrDockerCmd;

/**
 * @author Heng WU(wuheng09@otcaix.iscas.ac.cn)
 *
 */
public class StatsCmdImpl extends AbstrDockerCmd<StatsCmd, Stats> implements
		StatsCmd {

	private String containerId;

	public StatsCmdImpl(StatsCmd.Exec exec, String containerId) {
		super(exec);
		checkNotNull(containerId, "containerId was not specified");
		this.containerId = containerId;
	}

	public String getContainerId() {
		return containerId;
	}

	@Override
	public String toString() {
		return "stats";
	}

}
