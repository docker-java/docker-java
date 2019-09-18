package com.github.dockerjava.utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestResources {

    private TestResources() {
    }

    public static Path getApiImagesLoadTestTarball() {
        return Paths.get("src/test/resources/api/images/load/image.tar");
    }
}
