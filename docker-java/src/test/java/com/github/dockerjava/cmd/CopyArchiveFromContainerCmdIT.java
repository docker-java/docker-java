package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.core.util.CompressArchiveUtil;
import com.github.dockerjava.utils.TestUtils;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CopyArchiveFromContainerCmdIT extends CmdIT {
    public static final Logger LOG = LoggerFactory.getLogger(CopyArchiveFromContainerCmdIT.class);

    @Test
    public void copyFromContainer() throws Exception {
        // TODO extract this into a shared method
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName("copyFromContainer" + dockerRule.getKind())
                .withCmd("touch", "/copyFromContainer")
                .exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        InputStream response = dockerRule.getClient().copyArchiveFromContainerCmd(container.getId(), "/copyFromContainer").exec();

        // read the stream fully. Otherwise, the underlying stream will not be closed.
        String responseAsString = TestUtils.asString(response);
        assertNotNull(responseAsString);
        assertTrue(responseAsString.length() > 0);
    }

    @Test(expected = NotFoundException.class)
    public void copyFromNonExistingContainer() throws Exception {

        dockerRule.getClient().copyArchiveFromContainerCmd("non-existing", "/test").exec();
    }

    @Test
    public void copyFromContainerBinaryFile() throws Exception {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withName("copyFromContainerBinaryFile" + dockerRule.getKind())
                .exec();

        LOG.info("Created container: {}", container);
        assertThat(container.getId(), not(isEmptyOrNullString()));

        Path temp = Files.createTempFile("", ".tar.gz");
        Path binaryFile = Paths.get("src/test/resources/testCopyFromArchive/binary.dat");
        CompressArchiveUtil.tar(binaryFile, temp, true, false);

        try (InputStream uploadStream = Files.newInputStream(temp)) {
            dockerRule.getClient().copyArchiveToContainerCmd(container.getId()).withTarInputStream(uploadStream).exec();
        }

        InputStream response = dockerRule.getClient().copyArchiveFromContainerCmd(container.getId(), "/binary.dat").exec();

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
