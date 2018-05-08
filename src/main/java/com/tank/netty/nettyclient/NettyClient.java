package com.tank.netty.nettyclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	public void connect(String host, int port) throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.group(new NioEventLoopGroup());
//		bootstrap.option(option, value)
		bootstrap.handler(new ChildChannelHandler());
		ChannelFuture channelFuture = bootstrap.connect(host, port);
		channelFuture.channel().closeFuture().sync();
	}
}
