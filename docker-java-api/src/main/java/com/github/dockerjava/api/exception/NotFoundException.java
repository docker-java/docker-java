package com.github.dockerjava.api.exception;

/**
 * Indicates that the given entity does not exist.
 *
 * @author Ryan Campbell ryan.campbell@gmail.com
 */
public class NotFoundException extends DockerException {

    private static final long serialVersionUID = -2450396075981100160L;

    public NotFoundException(String message, Throwable cause) {
        super(message, 404, cause);
    }

    public NotFoundException(String message) {
        this(message, null);
    }

    public NotFoundException(Throwable cause) {
        this(cause.getMessage(), cause);
    }
}
