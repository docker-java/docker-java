package com.github.dockerjava.netty;

import io.netty.channel.Channel;

public interface ChannelProvider {
	Channel getChannel();
}
