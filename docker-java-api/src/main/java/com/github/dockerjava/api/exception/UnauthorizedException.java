package com.github.dockerjava.api.exception;

/**
 *
 */
public class UnauthorizedException extends DockerException {

    private static final long serialVersionUID = 8257731964780578278L;

    public UnauthorizedException(String message, Throwable cause) {
        super(message, 401, cause);
    }

    public UnauthorizedException(String message) {
        this(message, null);
    }

    public UnauthorizedException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
