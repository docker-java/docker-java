package com.github.dockerjava.jaxrs;

/*
 * Copyright (c) 2014 Spotify AB.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.newsclub.net.unix.AFUNIXSocketAddress;

/**
 * Provides a ConnectionSocketFactory for connecting Apache HTTP clients to Unix sockets.
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
class UnixConnectionSocketFactory implements ConnectionSocketFactory {

    private File socketFile;

    UnixConnectionSocketFactory(final URI socketUri) {
        super();

        final String filename = socketUri.toString().replaceAll("^unix:///", "unix://localhost/")
                .replaceAll("^unix://localhost", "");

        this.socketFile = new File(filename);
    }

    public static URI sanitizeUri(final URI uri) {
        if (uri.getScheme().equals("unix")) {
            return URI.create("unix://localhost:80");
        } else {
            return uri;
        }
    }

    @Override
    public Socket createSocket(final HttpContext context) throws IOException {
        return new ApacheUnixSocket();
    }

    @Override
    public Socket connectSocket(final int connectTimeout, final Socket socket, final HttpHost host,
            final InetSocketAddress remoteAddress, final InetSocketAddress localAddress, final HttpContext context)
            throws IOException {
        try {
            socket.connect(new AFUNIXSocketAddress(socketFile), connectTimeout);
        } catch (SocketTimeoutException e) {
            throw new ConnectTimeoutException(e, null, remoteAddress.getAddress());
        }

        return socket;
    }
}
