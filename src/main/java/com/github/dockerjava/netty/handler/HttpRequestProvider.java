package com.github.dockerjava.netty.handler;

import io.netty.handler.codec.http.HttpRequest;

public interface HttpRequestProvider {

    HttpRequest getHttpRequest(String uri);
}
