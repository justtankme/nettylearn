package com.tank.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
	public void start(int port) throws InterruptedException {
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup());
		serverBootstrap.channel(NioServerSocketChannel.class);
		serverBootstrap.childHandler(new ChildChannelHandler());
		System.out.println("服务端开启等待客户端连接 ... ...");
		serverBootstrap.bind(port).sync().channel().closeFuture().sync();
		
	}
}
