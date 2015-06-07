package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;

import com.github.dockerjava.api.command.PullImageCmd;
import com.github.dockerjava.api.model.AuthConfig;
import com.github.dockerjava.core.util.DockerImageName;

/**
 *
 * Pull image from repository.
 *
 */
public class PullImageCmdImpl extends AbstrAuthCfgDockerCmd<PullImageCmd, InputStream> implements PullImageCmd {

    private DockerImageName imageName;

	public PullImageCmdImpl(PullImageCmd.Exec exec, AuthConfig authConfig, DockerImageName imageName) {
		super(exec, authConfig);
		withImageName(imageName);
	}

    @Override
	public DockerImageName getImageName() {
        return imageName;
    }

	@Override
	public PullImageCmd withImageName(DockerImageName imageName) {
		checkNotNull(imageName, "imageName was not specified");
		this.imageName = imageName;
		return this;
	}

    @Override
    public String toString() {
        return new StringBuilder("pull ")
            .append(imageName)
            .toString();
    }
}
