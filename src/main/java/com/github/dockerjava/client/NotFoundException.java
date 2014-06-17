package com.github.dockerjava.client;

/**
 * Indicates that the given entity does not exist.
 *
 * @author Ryan Campbell ryan.campbell@gmail.com
 */
public class NotFoundException extends DockerException {

    public NotFoundException(String message) {
        super(message);
    }

}
