package com.github.dockerjava.api.exception;

/**
 *
 */
public class InternalServerErrorException extends DockerException {

    private static final long serialVersionUID = -2450396075981100160L;

    public InternalServerErrorException(String message, Throwable cause) {
        super(message, 500, cause);
    }

    public InternalServerErrorException(String message) {
        this(message, null);
    }

    public InternalServerErrorException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
