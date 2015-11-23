package com.github.dockerjava.jaxrs;

import static javax.ws.rs.client.Entity.entity;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dockerjava.api.command.CopyArchiveToContainerCmd;
import com.github.dockerjava.api.exception.DockerClientException;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.util.CompressArchiveUtil;

public class CopyArchiveToContainerCmdExec extends AbstrSyncDockerCmdExec<CopyArchiveToContainerCmd, Void> implements CopyArchiveToContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyArchiveFromContainerCmdExec.class);

    public CopyArchiveToContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    private InputStream buildUploadStream(CopyArchiveToContainerCmd command) throws IOException {
        Path toUpload = Files.createTempFile("docker-java", ".tar.gz");
        CompressArchiveUtil.tar(Paths.get(command.getHostResource()), toUpload, true, command.isDirChildrenOnly());
        return Files.newInputStream(toUpload);
    }

    @Override
    protected Void execute(CopyArchiveToContainerCmd command) {
        WebTarget webResource = getBaseResource().path("/containers/{id}/archive").resolveTemplate("id",
                command.getContainerId());

        LOGGER.trace("PUT: " + webResource.toString());
        try (InputStream streamToUpload = buildUploadStream(command)) {
            webResource
                    .queryParam("path", command.getRemotePath())
                    .queryParam("noOverwriteDirNonDir", command.isNoOverwriteDirNonDir())
                    .request()
                    .put(entity(streamToUpload, "application/x-tar"))
                    .close();
            return null;
        } catch (IOException e) {
            throw new DockerClientException("Error occurred while preparing uploading host resource <" + command.getHostResource() + ">", e);
        }
    }
}
