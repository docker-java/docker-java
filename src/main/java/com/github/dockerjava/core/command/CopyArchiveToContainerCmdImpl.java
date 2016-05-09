package com.github.dockerjava.core.command;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.util.CompressArchiveUtil;

public class CopyArchiveToContainerCmdImpl extends AbstrDockerCmd<CopyArchiveToContainerCmd, Void> implements
        CopyArchiveToContainerCmd {

    private String containerId;

    private String remotePath = ".";

    private InputStream tarInputStream;

    private String hostResource;

    private boolean noOverwriteDirNonDir = false;

    private boolean dirChildrenOnly = false;

    public CopyArchiveToContainerCmdImpl(CopyArchiveToContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public CopyArchiveToContainerCmd withContainerId(String containerId) {
        checkNotNull(containerId, "containerId was not specified");
        this.containerId = containerId;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withHostResource(String hostResource) {
        checkNotNull(hostResource, "hostResource was not specified");
        this.hostResource = hostResource;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withNoOverwriteDirNonDir(boolean noOverwriteDirNonDir) {
        this.noOverwriteDirNonDir = noOverwriteDirNonDir;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withRemotePath(String remotePath) {
        checkNotNull(remotePath, "remotePath was not specified");
        this.remotePath = remotePath;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withTarInputStream(InputStream tarInputStream) {
        checkNotNull(tarInputStream, "tarInputStream was not specified");
        this.tarInputStream = tarInputStream;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withDirChildrenOnly(boolean dirChildrenOnly) {
        this.dirChildrenOnly = dirChildrenOnly;
        return this;
    }

    @Override
    public InputStream getTarInputStream() {
        return tarInputStream;
    }

    @Override
    public String getContainerId() {
        return this.containerId;
    }

    @Override
    public String getHostResource() {
        return this.hostResource;
    }

    @Override
    public boolean isNoOverwriteDirNonDir() {
        return this.noOverwriteDirNonDir;
    }

    @Override
    public String getRemotePath() {
        return this.remotePath;
    }

    @Override
    public boolean isDirChildrenOnly() {
        return this.dirChildrenOnly;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cp ").append(hostResource).append(" ").append(containerId).append(":")
                .append(remotePath).toString();
    }

    private InputStream buildUploadStream(String hostResource, boolean dirChildrenOnly) throws IOException {
        Path toUpload = Files.createTempFile("docker-java", ".tar.gz");
        CompressArchiveUtil.tar(Paths.get(hostResource), toUpload, true, dirChildrenOnly);
        return Files.newInputStream(toUpload);
    }

    /**
     * @throws com.github.dockerjava.api.exception.NotFoundException
     *             No such container
     */
    @Override
    public Void exec() throws NotFoundException {
        if (StringUtils.isNotEmpty(this.hostResource)) {
            // User set host resource and not directly a stream
            if (this.tarInputStream != null) {
                throw new DockerClientException(
                        "Only one of host resource or tar input stream should be defined to perform the copy, not both");
            }
            // We compress the given path, call exec so that the stream is consumed and then close it our self
            try (InputStream uploadStream = buildUploadStream(this.hostResource, this.dirChildrenOnly)) {
                this.tarInputStream = uploadStream;
                return super.exec();
            } catch (IOException e) {
                throw new DockerClientException("Unable to perform tar on host resource " + this.hostResource, e);
            }
        } else if (this.tarInputStream == null) {
            throw new DockerClientException(
                    "One of host resource or tar input stream must be defined to perform the copy");
        }
        // User set a stream, so we will just consume it and let the user close it by him self
        return super.exec();
    }
}
