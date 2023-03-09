package com.github.dockerjava.core.command;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    private boolean copyUIDGID = false;

    public CopyArchiveToContainerCmdImpl(CopyArchiveToContainerCmd.Exec exec, String containerId) {
        super(exec);
        withContainerId(containerId);
    }

    @Override
    public CopyArchiveToContainerCmd withContainerId(String containerId) {
        this.containerId = Objects.requireNonNull(containerId, "containerId was not specified");
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withHostResource(String hostResource) {
        this.hostResource = Objects.requireNonNull(hostResource, "hostResource was not specified");
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withNoOverwriteDirNonDir(boolean noOverwriteDirNonDir) {
        this.noOverwriteDirNonDir = noOverwriteDirNonDir;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withCopyUIDGID(boolean copyUIDGID) {
        this.copyUIDGID = copyUIDGID;
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withRemotePath(String remotePath) {
        this.remotePath = Objects.requireNonNull(remotePath, "remotePath was not specified");
        return this;
    }

    @Override
    public CopyArchiveToContainerCmd withTarInputStream(InputStream tarInputStream) {
        this.tarInputStream = Objects.requireNonNull(tarInputStream, "tarInputStream was not specified");
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
    public boolean isCopyUIDGID() {
        return this.copyUIDGID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("cp ").append(String.format("-a=%b ", isCopyUIDGID()))
            .append(hostResource).append(" ").append(containerId).append(":").append(remotePath).toString();
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
            // create TAR package for the given path so docker can consume it
            Path toUpload = null;
            try {
                toUpload = Files.createTempFile("docker-java", ".tar.gz");
                CompressArchiveUtil.tar(Paths.get(hostResource), toUpload, true, dirChildrenOnly);
            } catch (IOException createFileIOException) {
                if (toUpload != null) {
                    // remove tmp docker-javaxxx.tar.gz
                    toUpload.toFile().delete();
                }
                throw new DockerClientException("Unable to perform tar on host resource " + this.hostResource, createFileIOException);
            }
            // send the tar stream, call exec so that the stream is consumed and then closed by try-with-resources
            try (InputStream uploadStream = Files.newInputStream(toUpload)) {
                this.tarInputStream = uploadStream;
                return super.exec();
            } catch (IOException e) {
                throw new DockerClientException("Unable to read temp file " + toUpload.toFile().getAbsolutePath(), e);
            } finally {
                this.tarInputStream = null;
                // remove tmp docker-javaxxx.tar.gz
                toUpload.toFile().delete();
            }
        } else if (this.tarInputStream == null) {
            throw new DockerClientException("One of host resource or tar input stream must be defined to perform the copy");
        }
        // User set a stream, so we will just consume it and let the user close it by him self
        return super.exec();
    }
}
