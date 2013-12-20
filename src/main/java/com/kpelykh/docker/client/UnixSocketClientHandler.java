package com.kpelykh.docker.client;

import com.sun.jersey.api.client.ClientHandler;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import jnr.unixsocket.UnixSocketAddress;
import jnr.unixsocket.UnixSocketChannel;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.nio.channels.Channels;

public class UnixSocketClientHandler implements ClientHandler {

  @Override
  public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
    System.out.println("UnixSocketClientHandler.handle " + cr);

    File path = new File("/var/run/docker.sock");

    String data = "GET /images/json?all=0 HTTP/1.1\n\n";

    UnixSocketAddress address = new UnixSocketAddress(path);
    UnixSocketChannel channel = null;
    try {
      channel = UnixSocketChannel.open(address);
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("connected to " + channel.getRemoteSocketAddress());
    PrintWriter w = new PrintWriter(Channels.newOutputStream(channel));
    w.print(data);
    w.flush();

    InputStreamReader r = new InputStreamReader(Channels.newInputStream(channel));

    CharBuffer result = CharBuffer.allocate(1024);
    try {
      r.read(result);
    } catch (IOException e) {
      e.printStackTrace();
    }
    result.flip();
    System.out.println("read from server: " + result.toString());

    return null;
  }
}
