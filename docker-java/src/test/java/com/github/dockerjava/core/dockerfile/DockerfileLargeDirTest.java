package com.github.dockerjava.core.dockerfile;

import com.google.common.base.Stopwatch;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DockerfileLargeDirTest {

    @Test
    public void shouldScanWithLargeExcludedSubdirectoryInLessThanThreeSeconds() throws IOException, URISyntaxException {
        final Path dockerfilePath = getDockerfilePath();
        final Dockerfile dockerfile = new Dockerfile(dockerfilePath.toFile(), dockerfilePath.getParent().toFile());
        Stopwatch stopwatch = com.google.common.base.Stopwatch.createStarted();
        Dockerfile.ScannedResult parse = dockerfile.parse();
        assertTrue("scan finishes in under 3 seconds", stopwatch.elapsed(TimeUnit.MILLISECONDS) < 3000);
        assertEquals("three files were added", 3, parse.filesToAdd.size());
    }

    @SneakyThrows
    private Path getDockerfilePath() throws URISyntaxException {
        return Paths.get(DockerfileLargeDirTest.class.getResource("/buildTests/largedir/Dockerfile").toURI());
    }

}
