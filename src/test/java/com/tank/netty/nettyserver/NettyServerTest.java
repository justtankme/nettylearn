package com.tank.netty.nettyserver;

import com.tank.constant.MyConstants;

public class NettyServerTest {
	public static void main(String[] args) {
		System.out.println("服务端开启等待客户端链接");
		try {
			NettyServer nettyServer = new NettyServer();
			nettyServer.bing(MyConstants.PORT_NETTY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}