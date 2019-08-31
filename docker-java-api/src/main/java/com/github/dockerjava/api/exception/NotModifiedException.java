package com.github.dockerjava.api.exception;

/**
 *
 */
public class NotModifiedException extends DockerException {

    private static final long serialVersionUID = -290093024775500239L;

    public NotModifiedException(String message, Throwable cause) {
        super(message, 304, cause);
    }

    public NotModifiedException(String message) {
        this(message, null);
    }

    public NotModifiedException(Throwable cause) {
        this(cause.getMessage(), cause);
    }

}
