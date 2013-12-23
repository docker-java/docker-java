package com.kpelykh.docker.client;

import org.apache.http.impl.io.AbstractSessionInputBuffer;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class UnixSocketSessionInputBuffer extends AbstractSessionInputBuffer {

  @Override
  protected void init(InputStream instream, int buffersize, HttpParams params) {
    super.init(instream, buffersize, params);
  }

  @Override
  public boolean isDataAvailable(int timeout) throws IOException {
    return true;
  }
}
