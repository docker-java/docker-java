package com.github.dockerjava.api.command;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;

public class ExportDockerImageTest {

    @Test
    public void shouldExportDockerImage() throws InterruptedException, IOException {
        String imageId = "busybox:latest";
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();

        dockerClient.pullImageCmd(imageId).start().awaitCompletion();
        InputStream result = dockerClient.exportImageCmd(imageId).exec();

        assertNotNull("The exported image tarball should not be null", result);
        assertHasData(result);
        assertContainsManifestJson();
    }

    private void assertHasData(InputStream result) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead = result.read(buffer);
        assertTrue("The exported image tarball should contain data", bytesRead > 0);
    }

    private void assertContainsManifestJson() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] exportedData = outputStream.toByteArray();
        String exportedContent = new String(exportedData);
        assertTrue("The exported image tarball should contain expected content",
                exportedContent.contains("manifest.json"));
    }

    private void totalBytesLargerThanZero(InputStream result, byte[] buffer, int bytesRead, ByteArrayOutputStream outputStream)
            throws IOException {
        int totalBytesRead = 0;
        while (bytesRead != -1) {
            outputStream.write(buffer, 0, bytesRead);
            totalBytesRead += bytesRead;
            bytesRead = result.read(buffer);
        }
        assertTrue("The exported image tarball should have a non-zero size", totalBytesRead > 0);
    }
}
