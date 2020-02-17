package com.github.dockerjava.api.exception;

/**
 *
 *
 */
public class DockerClientException extends RuntimeException {

    private static final long serialVersionUID = 7667768099261650608L;

    public DockerClientException(String message) {
        super(message);
    }

    public DockerClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
