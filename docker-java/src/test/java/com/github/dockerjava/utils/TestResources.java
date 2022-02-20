package com.github.dockerjava.utils;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestResources {

    private TestResources() {
    }

    public static Path getApiImagesLoadTestTarball() throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource("api/images/load/image.tar").toURI());
    }
}
