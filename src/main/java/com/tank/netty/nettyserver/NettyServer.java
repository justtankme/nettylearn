package com.tank.netty.nettyserver;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
	public void bing(int inetPort) throws InterruptedException {
		//提供给Channel注册用的线程池，线程数为NettyRuntime.availableProcessors() * 2与1的最大值，或者通过io.netty.eventLoopThreads配置
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		//快速创建channel的引导类
		ServerBootstrap bootstrap = new ServerBootstrap();
		//设置客户端和服务端的事件和IO处理池
		bootstrap.group(bossGroup, workGroup);
		//设置创建channel实例的类
		bootstrap.channel(NioServerSocketChannel.class);
		//设置backlog，即socket连接等待队列
		bootstrap.option(ChannelOption.SO_BACKLOG, 1);
		//设置channel中接收到的请求的处理类
		bootstrap.childHandler(new ChildChannelHandler());
		//创建一个channel，然后将其绑定到指定端口。等待该动作完成
		ChannelFuture channelFuture = bootstrap.bind(inetPort).sync();
		//关闭一个channel，等待该动作完成
		channelFuture.channel().closeFuture().sync();
		//关闭线程池
		bossGroup.shutdownGracefully();
		workGroup.shutdownGracefully();
	}
}
