package com.github.dockerjava.jaxrs.util;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.Response;

/**
 * This is a wrapper around {@link Response} that acts as a {@link InputStream}. When this {@link WrappedResponseInputStream} is closed it
 * closes the underlying {@link Response} object also to prevent blocking/hanging connections.
 *
 * @author Marcus Linke
 */
public class WrappedResponseInputStream extends InputStream {

    private Response response;

    private InputStream delegate;

    private boolean closed = false;

    public WrappedResponseInputStream(Response response) {
        this.response = response;
        this.delegate = response.readEntity(InputStream.class);
    }

    public int read() throws IOException {
        return delegate.read();
    }

    public int hashCode() {
        return delegate.hashCode();
    }

    public int read(byte[] b) throws IOException {
        return delegate.read(b);
    }

    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return delegate.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return delegate.skip(n);
    }

    public int available() throws IOException {
        return delegate.available();
    }

    public void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        delegate.close();
        response.close();
    }

    public void mark(int readlimit) {
        delegate.mark(readlimit);
    }

    public void reset() throws IOException {
        delegate.reset();
    }

    public boolean markSupported() {
        return delegate.markSupported();
    }

    public boolean isClosed() {
        return closed;
    }

}
