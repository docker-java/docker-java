package com.github.dockerjava.api.exception;

/**
 *
 */
public class NotAcceptableException extends DockerException {

    private static final long serialVersionUID = -1771212181727204375L;

    public NotAcceptableException(String message, Throwable cause) {
        super(message, 406, cause);
    }

    public NotAcceptableException(String message) {
        this(message, null);
    }

    public NotAcceptableException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
