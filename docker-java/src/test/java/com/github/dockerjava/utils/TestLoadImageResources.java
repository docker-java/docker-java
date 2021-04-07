package com.github.dockerjava.utils;


import java.nio.file.Path;
import java.nio.file.Paths;

public class TestLoadImageResources {

    private TestLoadImageResources() {
    }

    public static Path getLoadImageIdTestTarball() {
        return Paths.get("src/test/resources/api/images/loadAsync/image-id.tar");
    }

    public static Path getLoadImageTagTestTarball() {
        return Paths.get("src/test/resources/api/images/loadAsync/image-tag.tar");
    }

}
