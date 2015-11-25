package com.github.dockerjava.core.util;

import java.io.File;
import java.io.IOException;

import com.github.dockerjava.api.exception.DockerClientException;

public class FilePathUtil {
    private FilePathUtil() {
        // utility class
    }

    /**
     * Return the relative path. Path elements are separated with / char.
     * 
     * @param baseDir
     *            a parent directory of {@code file}
     * @param file
     *            the file to get the relative path
     * @return the relative path
     */
    public static String relativize(File baseDir, File file) {
        try {
            baseDir = baseDir.getCanonicalFile();
            file = file.getCanonicalFile();

            return baseDir.toURI().relativize(file.toURI()).getPath();
        } catch (IOException e) {
            throw new DockerClientException(e.getMessage(), e);
        }
    }
}
