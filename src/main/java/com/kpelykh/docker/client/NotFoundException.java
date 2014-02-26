package com.kpelykh.docker.client;

/**
 * Indicates that the given entity does not exist.
 *
 * @author Ryan Campbell ryan.campbell@gmail.com
 */
public class NotFoundException extends DockerException {
    public NotFoundException() {

    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
