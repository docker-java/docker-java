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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;

import org.newsclub.net.unix.AFUNIXSocket;

/**
 * Provides a socket that wraps an org.newsclub.net.unix.AFUNIXSocket and delays setting options until the socket is connected. This is
 * necessary because the Apache HTTP client attempts to set options prior to connecting the socket, which doesn't work for Unix sockets
 * since options are being set on the underlying file descriptor. Until the socket is connected, the file descriptor doesn't exist.
 *
 * This class also noop's any calls to setReuseAddress, which is called by the Apache client but isn't supported by AFUnixSocket.
 */
class ApacheUnixSocket extends Socket {

    private final AFUNIXSocket inner;

    private final Queue<SocketOptionSetter> optionsToSet = new ArrayDeque<>();

    ApacheUnixSocket() throws IOException {
        this.inner = AFUNIXSocket.newInstance();
    }

    @Override
    public void connect(final SocketAddress endpoint) throws IOException {
        inner.connect(endpoint);
        setAllSocketOptions();
    }

    @Override
    public void connect(final SocketAddress endpoint, final int timeout) throws IOException {
        inner.connect(endpoint, timeout);
        setAllSocketOptions();
    }

    @Override
    public void bind(final SocketAddress bindpoint) throws IOException {
        inner.bind(bindpoint);
        setAllSocketOptions();
    }

    @Override
    public InetAddress getInetAddress() {
        return inner.getInetAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        return inner.getLocalAddress();
    }

    @Override
    public int getPort() {
        return inner.getPort();
    }

    @Override
    public int getLocalPort() {
        return inner.getLocalPort();
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
        return inner.getRemoteSocketAddress();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
        return inner.getLocalSocketAddress();
    }

    @Override
    public SocketChannel getChannel() {
        return inner.getChannel();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return inner.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return inner.getOutputStream();
    }

    private void setSocketOption(final SocketOptionSetter s) throws SocketException {
        if (inner.isConnected()) {
            s.run();
        } else {
            if (!optionsToSet.offer(s)) {
                throw new SocketException("Failed to queue option");
            }
        }
    }

    private void setAllSocketOptions() throws SocketException {
        for (SocketOptionSetter s : optionsToSet) {
            s.run();
        }
    }

    @Override
    public void setTcpNoDelay(final boolean on) throws SocketException {
        setSocketOption(() -> inner.setTcpNoDelay(on));
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
        return inner.getTcpNoDelay();
    }

    @Override
    public void setSoLinger(final boolean on, final int linger) throws SocketException {
        setSocketOption(() -> inner.setSoLinger(on, linger));
    }

    @Override
    public int getSoLinger() throws SocketException {
        return inner.getSoLinger();
    }

    @Override
    public void sendUrgentData(final int data) throws IOException {
        inner.sendUrgentData(data);
    }

    @Override
    public void setOOBInline(final boolean on) throws SocketException {
        setSocketOption(() -> inner.setOOBInline(on));
    }

    @Override
    public boolean getOOBInline() throws SocketException {
        return inner.getOOBInline();
    }

    @Override
    public synchronized void setSoTimeout(final int timeout) throws SocketException {
        setSocketOption(() -> inner.setSoTimeout(timeout));
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException {
        return inner.getSoTimeout();
    }

    @Override
    public synchronized void setSendBufferSize(final int size) throws SocketException {
        setSocketOption(() -> inner.setSendBufferSize(size));
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException {
        return inner.getSendBufferSize();
    }

    @Override
    public synchronized void setReceiveBufferSize(final int size) throws SocketException {
        setSocketOption(() -> inner.setReceiveBufferSize(size));
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
        return inner.getReceiveBufferSize();
    }

    @Override
    public void setKeepAlive(final boolean on) throws SocketException {
        setSocketOption(() -> inner.setKeepAlive(on));
    }

    @Override
    public boolean getKeepAlive() throws SocketException {
        return inner.getKeepAlive();
    }

    @Override
    public void setTrafficClass(final int tc) throws SocketException {
        setSocketOption(() -> inner.setTrafficClass(tc));
    }

    @Override
    public int getTrafficClass() throws SocketException {
        return inner.getTrafficClass();
    }

    @Override
    public void setReuseAddress(final boolean on) throws SocketException {
        // not supported: Apache client tries to set it, but we want to just ignore it
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
        return inner.getReuseAddress();
    }

    @Override
    public synchronized void close() throws IOException {
        inner.close();
    }

    @Override
    public void shutdownInput() throws IOException {
        inner.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException {
        inner.shutdownOutput();
    }

    @Override
    public String toString() {
        return inner.toString();
    }

    @Override
    public boolean isConnected() {
        return inner.isConnected();
    }

    @Override
    public boolean isBound() {
        return inner.isBound();
    }

    @Override
    public boolean isClosed() {
        return inner.isClosed();
    }

    @Override
    public boolean isInputShutdown() {
        return inner.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown() {
        return inner.isOutputShutdown();
    }

    @Override
    public void setPerformancePreferences(final int connectionTime, final int latency, final int bandwidth) {
        inner.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    interface SocketOptionSetter {
        void run() throws SocketException;
    }
}
