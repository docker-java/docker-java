package com.github.dockerjava.core.command;

import com.github.dockerjava.api.command.BuildImageCmd;
import com.github.dockerjava.api.model.AuthConfigurations;
import com.github.dockerjava.api.model.BuildResponseItem;
import com.github.dockerjava.core.dockerfile.Dockerfile;
import com.github.dockerjava.core.util.FilePathUtil;

import javax.annotation.CheckForNull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Build an image from Dockerfile.
 */
public class BuildImageCmdImpl extends AbstrAsyncDockerCmd<BuildImageCmd, BuildResponseItem> implements BuildImageCmd {

    private InputStream tarInputStream;

    @Deprecated
    private String tag;

    private Set<String> tags;

    private Set<String> cacheFrom;

    private Boolean noCache;

    private Boolean remove = true;

    private Boolean quiet;

    private Boolean pull;

    private AuthConfigurations buildAuthConfigs;

    private File dockerFile;

    private String dockerFilePath;

    private File baseDirectory;

    private String cpusetcpus;

    private Long memory;

    private String cpushares;

    private Boolean forcerm;

    private Long memswap;

    private Long shmsize;

    private URI remote;

    private Map<String, String> buildArgs;

    private Map<String, String> labels;

    private String networkMode;

    private String platform;

    private String target;

    private Set<String> extraHosts;

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

    @Deprecated
    @Override
    public String getTag() {
        return tag;
    }

    @CheckForNull
    public Set<String> getTags() {
        return tags;
    }

    @CheckForNull
    public Set<String> getCacheFrom() {
       return cacheFrom;
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
        if (dockerFilePath != null) {
            return dockerFilePath;
        } else if (baseDirectory != null && dockerFile != null) {
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

    @Override
    public Map<String, String> getBuildArgs() {
        return buildArgs;
    }

    @Override
    public Map<String, String> getLabels() {
        return labels;
    }

    @Override
    public String getNetworkMode() {
        return networkMode;
    }

    @Override
    public String getPlatform() {
        return platform;
    }

    @Override
    public String getTarget() {
        return target;
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

    /**
     * @see #shmsize
     */
    @Override
    public Long getShmsize() {
        return shmsize;
    }

    @Override
    public Set<String> getExtraHosts() {
        return extraHosts;
    }

    // setters

    /**
     * @deprecated use #withTags()
     */
    @Deprecated
    @Override
    public BuildImageCmdImpl withTag(String tag) {
        checkNotNull(tag, "Tag is null");
        this.tag = tag;
        return this;
    }

    @Override
    public BuildImageCmd withTags(Set<String> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public BuildImageCmd withCacheFrom(Set<String> cacheFrom) {
        this.cacheFrom = cacheFrom;
        return this;
    }

    @Override
    public BuildImageCmd withRemote(URI remote) {
        this.remote = remote;
        return this;
    }

    @Override
    public BuildImageCmdImpl withNoCache(Boolean noCache) {
        this.noCache = noCache;
        return this;
    }

    @Override
    public BuildImageCmdImpl withRemove(Boolean rm) {
        this.remove = rm;
        return this;
    }

    @Override
    public BuildImageCmd withForcerm(Boolean forcerm) {
        this.forcerm = forcerm;
        return this;
    }

    @Override
    public BuildImageCmdImpl withQuiet(Boolean quiet) {
        this.quiet = quiet;
        return this;
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

    @Override
    public BuildImageCmd withBuildArg(String key, String value) {
        if (this.buildArgs == null) {
            this.buildArgs = new HashMap<>();
        }
        this.buildArgs.put(key, value);
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
        if (!dockerfile.exists()) {
            throw new IllegalArgumentException("Dockerfile does not exist");
        }
        if (!dockerfile.isFile()) {
            throw new IllegalArgumentException("Dockerfile is not a file");
        }

        if (baseDirectory == null) {
            withBaseDirectory(dockerfile.getParentFile());
        }

        this.dockerFile = dockerfile;

        try {
            withTarInputStream(new Dockerfile(dockerfile, baseDirectory).parse().buildDockerFolderTar());
        } catch (IOException e) {
            // we just created the file this should never happen.
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public BuildImageCmd withDockerfilePath(String dockerfilePath) {
        checkNotNull(dockerfilePath, "dockerfilePath is null");
        this.dockerFilePath = dockerfilePath;
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

    /**
     * @see #shmsize
     */
    @Override
    public BuildImageCmd withShmsize(Long shmsize) {
        this.shmsize = shmsize;
        return this;
    }

    /**
     * @see #labels
     */
    @Override
    public BuildImageCmd withLabels(Map<String, String> labels) {
        this.labels = labels;
        return this;
    }

    @Override
    public BuildImageCmd withNetworkMode(String networkMode) {
        this.networkMode = networkMode;
        return this;
    }

    @Override
    public BuildImageCmd withPlatform(String platform) {
        this.platform = platform;
        return this;
    }

    @Override
    public BuildImageCmd withTarget(String target) {
        this.target = target;
        return this;
    }

    @Override
    public BuildImageCmd withExtraHosts(Set<String> extraHosts) {
        this.extraHosts = extraHosts;
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
