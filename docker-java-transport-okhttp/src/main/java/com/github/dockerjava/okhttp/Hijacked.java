package com.github.dockerjava.okhttp;

import java.io.InputStream;

class Hijacked {

    private final InputStream inputStream;

    Hijacked(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
