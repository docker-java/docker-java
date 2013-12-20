package com.kpelykh.docker.client;

import com.sun.jersey.api.client.Client;


public class UnixSocketClient extends Client {

  public UnixSocketClient() {
    super(new UnixSocketClientHandler());
  }
}
