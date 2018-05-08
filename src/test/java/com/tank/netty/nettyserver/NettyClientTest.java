package com.tank.netty.nettyserver;

import com.tank.constant.MyConstants;
import com.tank.netty.nettyclient.NettyClient;

public class NettyClientTest {
	public static void main(String[] args) {
		NettyClient nettyClient = new NettyClient();
		try {
			nettyClient.connect(MyConstants.HOST, MyConstants.PORT_NETTY);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
