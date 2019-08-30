package com.github.dockerjava.api.exception;

/**
 *
 */
public class ConflictException extends DockerException {

    private static final long serialVersionUID = -290093024775500239L;

    public ConflictException(String message, Throwable cause) {
        super(message, 409, cause);
    }

    public ConflictException(String message) {
        this(message, null);
    }

    public ConflictException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
