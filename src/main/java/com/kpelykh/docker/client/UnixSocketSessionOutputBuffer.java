package com.kpelykh.docker.client;

import org.apache.http.impl.io.AbstractSessionOutputBuffer;
import org.apache.http.params.HttpParams;

import java.io.OutputStream;

/**
 * {@link org.apache.http.impl.io.AbstractSessionOutputBuffer} implementation for UNIX sockets.
 */
public class UnixSocketSessionOutputBuffer extends AbstractSessionOutputBuffer {

  @Override
  protected void init(OutputStream outstream, int buffersize, HttpParams params) {
    super.init(outstream, buffersize, params);
  }
}
