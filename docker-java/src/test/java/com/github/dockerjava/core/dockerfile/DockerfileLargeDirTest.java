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

public class DockerfileLargeDirTest {

    @Test
    public void shouldScanWithLargeExcludedSubdirectoryInLessThanThreeSeconds() throws IOException, URISyntaxException {
        final Path dockerfilePath = getDockerfilePath();
        final Dockerfile dockerfile = new Dockerfile(dockerfilePath.toFile(), dockerfilePath.getParent().toFile());
        Stopwatch stopwatch = com.google.common.base.Stopwatch.createStarted();
        Dockerfile.ScannedResult parse = dockerfile.parse();
        assertEquals("scan finishes in under 5 seconds", 5000,  Math.max(stopwatch.elapsed(TimeUnit.MILLISECONDS), 5000));
        assertEquals("9 files were added", 9, parse.filesToAdd.size());
    }

    @SneakyThrows
    private Path getDockerfilePath() throws URISyntaxException {
        return Paths.get(DockerfileLargeDirTest.class.getResource("/buildTests/largedir/Dockerfile").toURI());
    }

}
