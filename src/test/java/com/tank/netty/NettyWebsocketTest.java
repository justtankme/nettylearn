package com.tank.netty;

import com.tank.constant.MyConstants;
import com.tank.netty.websocket.NettyServer;

public class NettyWebsocketTest {
	public static void main(String[] args) {
		try {
			NettyServer nettyServer = new NettyServer();
			nettyServer.start(MyConstants.PORT_NETTY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}