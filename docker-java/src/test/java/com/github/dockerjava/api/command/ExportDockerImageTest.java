package com.github.dockerjava.api.command;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

public class ExportDockerImageTest {

    @Test
    public void shouldExportDockerImage() throws InterruptedException, IOException {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        File dockerfileStream = getDockerfileFromResources();
        String imageId = buildDockerImage(dockerClient, dockerfileStream);
        InputStream result = dockerClient.exportImageCmd(imageId).exec();

        assertNotNull("The exported image tarball should not be null", result);
        assertHasData(result);
        assertContainsManifestJson(result);
    }

    private String buildDockerImage(DockerClient dockerClient, File dockerfileStream) {
        String imageId = dockerClient.buildImageCmd()
            .withDockerfile(dockerfileStream)
            .withTags(Set.of("busybox:latest"))
            .start()
            .awaitImageId();
        return imageId;
    }

    private File getDockerfileFromResources() {
        File dockerfileStream = Paths.get("src/test/resources/busyboxDockerfile/Dockerfile").toFile();
        assertNotNull("Dockerfile should be present in the resources folder", dockerfileStream);
        return dockerfileStream;
    }

    private void assertHasData(InputStream result) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = result.read(buffer);
        assertTrue("The exported image tarball should contain data", bytesRead > 0);
    }

    private void assertContainsManifestJson(InputStream result) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = result.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        String exportedContent = outputStream.toString();
        assertTrue("The exported image tarball should contain expected content",
                exportedContent.contains("manifest.json"));
    }

}
