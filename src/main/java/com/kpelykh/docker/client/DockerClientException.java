package com.kpelykh.docker.client;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */

public class DockerClientException extends Exception {

    public DockerClientException() {
    }

    public DockerClientException(String message) {
        super(message);
    }

    public DockerClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public DockerClientException(Throwable cause) {
        super(cause);
    }
}
