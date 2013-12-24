package com.kpelykh.docker.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;


public class UnixSocketClient extends Client {

  public UnixSocketClient(ClientConfig clientConfig) {
    super(new UnixSocketClientHandler(), clientConfig);
  }
}
