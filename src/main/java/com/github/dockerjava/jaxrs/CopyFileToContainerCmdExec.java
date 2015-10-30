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

import com.github.dockerjava.api.DockerClientException;
import com.github.dockerjava.core.CompressArchiveUtil;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.CopyFileToContainerCmd;

public class CopyFileToContainerCmdExec extends AbstrSyncDockerCmdExec<CopyFileToContainerCmd, Void> implements CopyFileToContainerCmd.Exec {

    private static final Logger LOGGER = LoggerFactory.getLogger(CopyFileFromContainerCmdExec.class);

    public CopyFileToContainerCmdExec(WebTarget baseResource, DockerClientConfig dockerClientConfig) {
        super(baseResource, dockerClientConfig);
    }

    private InputStream buildUploadStream(CopyFileToContainerCmd command) throws IOException {
        Path toUpload = Files.createTempFile("docker-java", ".tar.gz");
        CompressArchiveUtil.tar(Paths.get(command.getHostResource()), toUpload, true, command.isDirChildrenOnly());
        return Files.newInputStream(toUpload);
    }

    @Override
    protected Void execute(CopyFileToContainerCmd command) {
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
