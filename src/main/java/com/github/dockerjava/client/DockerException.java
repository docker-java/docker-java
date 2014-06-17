package com.github.dockerjava.client;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */

public class DockerException extends RuntimeException {

    public DockerException() {
    }

    public DockerException(String message) {
        super(message);
    }

    public DockerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DockerException(Throwable cause) {
        super(cause);
    }
}
