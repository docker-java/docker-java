package com.github.dockerjava.netty;

import io.netty.channel.socket.DuplexChannel;

public interface ChannelProvider {
    DuplexChannel getChannel();
}
