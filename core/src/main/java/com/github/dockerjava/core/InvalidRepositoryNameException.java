package com.github.dockerjava.core;

public class InvalidRepositoryNameException extends IllegalArgumentException {

    private static final long serialVersionUID = -6908709623436840513L;

    public InvalidRepositoryNameException() {
        super();
    }

    public InvalidRepositoryNameException(String s) {
        super(s);
    }

}
