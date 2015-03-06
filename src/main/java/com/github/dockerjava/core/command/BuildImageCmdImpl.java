package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.core.dockerfile.Dockerfile;

/**
 * 
 * Build an image from Dockerfile.
 * 
 */
public class BuildImageCmdImpl extends AbstrDockerCmd<BuildImageCmd, BuildImageCmd.Response> implements BuildImageCmd {


	private InputStream tarInputStream = null;
	private String tag;
	private boolean noCache;
	private boolean remove = true;
	private boolean quiet;
	private AuthConfigurations buildAuthConfigs;

	public BuildImageCmdImpl(BuildImageCmd.Exec exec, File dockerFolder) {
		super(exec);
		checkNotNull(dockerFolder, "dockerFolder is null");

		try {
			withTarInputStream(
                            new Dockerfile(new File(dockerFolder, "Dockerfile"))
                                          .parse()
                                          .buildDockerFolderTar() );
		} catch (IOException e) {
			// we just created the file this should never happen.
			throw new RuntimeException(e);
		}
	}

	public BuildImageCmdImpl(BuildImageCmd.Exec exec, InputStream tarInputStream) {
		super(exec);
		checkNotNull(tarInputStream, "tarInputStream is null");
		withTarInputStream(tarInputStream);
	}

	@Override
	public InputStream getTarInputStream() {
		return tarInputStream;
	}

	@Override
	public BuildImageCmdImpl withTarInputStream(InputStream tarInputStream) {
		checkNotNull(tarInputStream, "tarInputStream is null");
		this.tarInputStream = tarInputStream;
		return this;
	}

	@Override
	public BuildImageCmdImpl withTag(String tag) {
		checkNotNull(tag, "Tag is null");
		this.tag = tag;
		return this;
	}

	@Override
	public String getTag() {
		return tag;
	}

	@Override
	public boolean hasNoCacheEnabled() {
		return noCache;
	}

	@Override
	public boolean hasRemoveEnabled() {
		return remove;
	}

	@Override
	public boolean isQuiet() {
		return quiet;
	}

	@Override
	public AuthConfigurations getBuildAuthConfigs() {
		return buildAuthConfigs;
	}

	@Override
	public BuildImageCmdImpl withNoCache() {
		return withNoCache(true);
	}

	@Override
	public BuildImageCmdImpl withNoCache(boolean noCache) {
		this.noCache = noCache;
		return this;
	}

	@Override
	public BuildImageCmdImpl withRemove() {
		return withRemove(true);
	}

	@Override
	public BuildImageCmdImpl withRemove(boolean rm) {
		this.remove = rm;
		return this;
	}

	@Override
	public BuildImageCmdImpl withQuiet() {
		return withQuiet(true);
	}

	@Override
	public BuildImageCmdImpl withQuiet(boolean quiet) {
		this.quiet = quiet;
		return this;
	}

	@Override
	public BuildImageCmd withBuildAuthConfigs(AuthConfigurations authConfigs) {
		checkNotNull(authConfigs, "authConfig is null");
		this.buildAuthConfigs = authConfigs;
		return this;
	}

	@Override
	public void close() throws IOException {
		super.close();

		tarInputStream.close();
	}

	@Override
	public String toString() {
		return new StringBuilder("build ")
				.append(tag != null ? "-t " + tag + " " : "")
				.append(noCache ? "--nocache=true " : "")
				.append(quiet ? "--quiet=true " : "")
				.append(!remove ? "--rm=false " : "").toString();
	}


}
