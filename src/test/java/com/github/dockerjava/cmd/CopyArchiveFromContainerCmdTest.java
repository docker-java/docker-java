package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.AbstractJerseyDockerClientTest;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.github.dockerjava.junit.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;

public class CopyArchiveFromContainerCmdTest extends AbstractJerseyDockerClientTest {

    @Test
    public void copyFromContainer() throws Exception {
        // TODO extract this into a shared method
        CreateContainerResponse container = dockerClient.createContainerCmd(DEFAULT_IMAGE)
                .withName("docker-java-itest-copyFromContainer").withCmd("touch", "/copyFromContainer").exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerClient.startContainerCmd(container.getId()).exec();

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "/copyFromContainer").exec();
        Boolean bytesAvailable = response.available() > 0;
        assertTrue("The file was not copied from the container.", bytesAvailable );

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test(expected = NotFoundException.class)
    public void copyFromNonExistingContainer() throws Exception {

        dockerClient.copyArchiveFromContainerCmd("non-existing", "/test").exec();
    }

    @Test
    public void copyFromContainerBinaryFile() throws Exception {
        CreateContainerResponse container = dockerClient.createContainerCmd(DEFAULT_IMAGE)
                .withName("docker-java-itest-copyFromContainerBinaryFile").exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        Path temp = Files.createTempFile("", ".tar.gz");
        Path binaryFile = Paths.get("src/test/resources/testCopyFromArchive/binary.dat");
        CompressArchiveUtil.tar(binaryFile, temp, true, false);

        try (InputStream uploadStream = Files.newInputStream(temp)) {
            dockerClient.copyArchiveToContainerCmd(container.getId()).withTarInputStream(uploadStream).exec();
        }

        InputStream response = dockerClient.copyArchiveFromContainerCmd(container.getId(), "/binary.dat").exec();
        Boolean bytesAvailable = response.available() > 0;
        assertTrue("The file was not copied from the container.", bytesAvailable);

        try (TarArchiveInputStream tarInputStream = new TarArchiveInputStream(response)) {
            TarArchiveEntry nextTarEntry = tarInputStream.getNextTarEntry();

            assertEquals(nextTarEntry.getName(), "binary.dat");
            try (InputStream binaryFileInputStream = Files.newInputStream(binaryFile, StandardOpenOption.READ)) {
                assertTrue(IOUtils.contentEquals(binaryFileInputStream, tarInputStream));
            }

            assertNull("Nothing except binary.dat is expected to be copied.", tarInputStream.getNextTarEntry());
        }
    }
}
