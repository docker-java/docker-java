package com.github.dockerjava.cmd;

import com.github.dockerjava.api.command.CreateContainerResponse;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

import static com.github.dockerjava.core.DockerRule.DEFAULT_IMAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;

public class ExportContainerCmdIT extends CmdIT {

    private static final Logger LOG = LoggerFactory.getLogger(ExportContainerCmdIT.class);

    @Test
    public void exportContainerHasCreatedFile() throws Exception {
        CreateContainerResponse container = dockerRule.getClient().createContainerCmd(DEFAULT_IMAGE)
                .withCmd("touch", "/myExportedFile")
                .exec();

        LOG.info("Created container: {}", container.toString());
        assertThat(container.getId(), not(is(emptyString())));

        dockerRule.getClient().startContainerCmd(container.getId()).exec();

        int exitCode = dockerRule.getClient().waitContainerCmd(container.getId()).start()
                .awaitStatusCode();
        assertThat(exitCode, is(0));

        try (InputStream response = dockerRule.getClient().exportContainerCmd(container.getId()).exec()) {
            boolean foundFile = false;
            try (TarArchiveInputStream tarStream = new TarArchiveInputStream(response)) {
                TarArchiveEntry entry;
                while ((entry = tarStream.getNextTarEntry()) != null) {
                    if (entry.getName().contains("myExportedFile")) {
                        foundFile = true;
                        break;
                    }
                }
            }
            assertThat("Exported archive should contain the created file", foundFile, is(true));
        }
    }

}
