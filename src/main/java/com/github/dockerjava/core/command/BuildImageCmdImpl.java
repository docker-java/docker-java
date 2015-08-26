package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.annotation.CheckForNull;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.FilePathUtil;
import com.github.dockerjava.core.dockerfile.Dockerfile;

/**
 *
 * Build an image from Dockerfile.
 *
 */
public class BuildImageCmdImpl extends AbstrAsyncDockerCmd<BuildImageCmd, BuildResponseItem> implements BuildImageCmd {

    @CheckForNull
    private InputStream tarInputStream;

    @CheckForNull
    private String tag;

    private Boolean noCache;

    private Boolean remove = true;

    private Boolean quiet;

    private Boolean pull;

    @CheckForNull
    private AuthConfigurations buildAuthConfigs;

    @CheckForNull
    private File dockerFile;

    @CheckForNull
    private File baseDirectory;

    @CheckForNull
    private String cpusetcpus;

    @CheckForNull
    private Long memory;

    @CheckForNull
    private String cpushares;

    @CheckForNull
    private Boolean forcerm;

    @CheckForNull
    private Long memswap;

    @CheckForNull
    private URI remote;

    public BuildImageCmdImpl(BuildImageCmd.Exec exec) {
        super(exec);
    }

    public BuildImageCmdImpl(BuildImageCmd.Exec exec, File dockerFileOrFolder) {
        super(exec);
        checkNotNull(dockerFileOrFolder, "dockerFolder is null");

        if (dockerFileOrFolder.isDirectory()) {
            withBaseDirectory(dockerFileOrFolder);
            withDockerfile(new File(dockerFileOrFolder, "Dockerfile"));
        } else {
            withDockerfile(dockerFileOrFolder);
        }
    }

    public BuildImageCmdImpl(BuildImageCmd.Exec exec, InputStream tarInputStream) {
        super(exec);
        checkNotNull(tarInputStream, "tarInputStream is null");
        withTarInputStream(tarInputStream);
    }

    // getters API

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public URI getRemote() {
        return remote;
    }

    @Override
    public Boolean hasNoCacheEnabled() {
        return noCache;
    }

    @Override
    public Boolean hasRemoveEnabled() {
        return remove;
    }

    @Override
    public Boolean isForcerm() {
        return forcerm != null && forcerm;
    }

    @Override
    public Boolean getForcerm() {
        return forcerm;
    }

    @Override
    public Boolean isQuiet() {
        return quiet;
    }

    @Override
    public Boolean hasPullEnabled() {
        return pull;
    }

    @Override
    public String getPathToDockerfile() {
        if (baseDirectory != null && dockerFile != null) {
            return FilePathUtil.relativize(baseDirectory, dockerFile);
        } else {
            return null;
        }
    }

    @Override
    public Long getMemory() {
        return memory;
    }

    @Override
    public Long getMemswap() {
        return memswap;
    }

    @Override
    public String getCpushares() {
        return cpushares;
    }

    @Override
    public String getCpusetcpus() {
        return cpusetcpus;
    }

    // getter lib specific

    @Override
    public AuthConfigurations getBuildAuthConfigs() {
        return buildAuthConfigs;
    }

    @Override
    public InputStream getTarInputStream() {
        return tarInputStream;
    }

    // setters

    @Override
    public BuildImageCmdImpl withTag(String tag) {
        checkNotNull(tag, "Tag is null");
        this.tag = tag;
        return this;
    }

    @Override
    public BuildImageCmd withRemote(URI remote) {
        this.remote = remote;
        return this;
    }

    @Override
    public BuildImageCmdImpl withNoCache() {
        return withNoCache(true);
    }

    @Override
    public BuildImageCmdImpl withNoCache(Boolean noCache) {
        this.noCache = noCache;
        return this;
    }

    @Override
    public BuildImageCmdImpl withRemove() {
        return withRemove(true);
    }

    @Override
    public BuildImageCmdImpl withRemove(Boolean rm) {
        this.remove = rm;
        return this;
    }

    @Override
    public BuildImageCmd withForcerm() {
        return withForcerm(true);
    }

    @Override
    public BuildImageCmd withForcerm(Boolean forcerm) {
        this.forcerm = forcerm;
        return this;
    }

    @Override
    public BuildImageCmdImpl withQuiet() {
        return withQuiet(true);
    }

    @Override
    public BuildImageCmdImpl withQuiet(Boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    @Override
    public BuildImageCmdImpl withPull() {
        return withPull(true);
    }

    @Override
    public BuildImageCmdImpl withPull(Boolean pull) {
        this.pull = pull;
        return this;
    }

    @Override
    public BuildImageCmd withMemory(Long memory) {
        this.memory = memory;
        return this;
    }

    @Override
    public BuildImageCmd withMemswap(Long memswap) {
        this.memswap = memswap;
        return this;
    }

    @Override
    public BuildImageCmd withCpushares(String cpushares) {
        this.cpushares = cpushares;
        return this;
    }

    @Override
    public BuildImageCmd withCpusetcpus(String cpusetcpus) {
        this.cpusetcpus = cpusetcpus;
        return this;
    }

    // lib specific

    @Override
    public BuildImageCmd withBaseDirectory(File baseDirectory) {
        this.baseDirectory = baseDirectory;
        return this;
    }

    @Override
    public BuildImageCmdImpl withDockerfile(File dockerfile) {
        checkNotNull(dockerfile);
        if (!dockerfile.exists())
            throw new IllegalArgumentException("Dockerfile does not exist");
        if (!dockerfile.isFile())
            throw new IllegalArgumentException("Not a directory");

        if (baseDirectory == null)
            withBaseDirectory(dockerfile.getParentFile());

        this.dockerFile = dockerfile;

        try {
            withTarInputStream(new Dockerfile(dockerfile).parse().buildDockerFolderTar(baseDirectory));
        } catch (IOException e) {
            // we just created the file this should never happen.
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public BuildImageCmdImpl withTarInputStream(InputStream tarInputStream) {
        checkNotNull(tarInputStream, "tarInputStream is null");
        this.tarInputStream = tarInputStream;
        return this;
    }

    @Override
    public BuildImageCmd withBuildAuthConfigs(AuthConfigurations authConfigs) {
        checkNotNull(authConfigs, "authConfig is null");
        this.buildAuthConfigs = authConfigs;
        return this;
    }

    @Override
    public void close() {
        super.close();

        try {
            tarInputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
