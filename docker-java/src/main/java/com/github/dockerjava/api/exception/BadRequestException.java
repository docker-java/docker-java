package com.github.dockerjava.api.exception;

/**
 *
 */
public class BadRequestException extends DockerException {

    private static final long serialVersionUID = -2450396075981100160L;

    public BadRequestException(String message, Throwable cause) {
        super(message, 400, cause);
    }

    public BadRequestException(String message) {
        this(message, null);
    }

    public BadRequestException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
