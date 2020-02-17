package com.github.dockerjava.api.exception;

/**
 *
 * @author Konstantin Pelykh (kpelykh@gmail.com)
 *
 */

public class DockerException extends RuntimeException {

    private static final long serialVersionUID = 7667768099261650608L;

    private int httpStatus = 0;

    public DockerException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public DockerException(String message, int httpStatus, Throwable cause) {
        super(message, cause);
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
