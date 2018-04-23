package com.tank.nio;

import com.tank.constant.MyConstants;
import com.tank.nio.socketchannel.ClientNIO;
import com.tank.nio.socketchannel.ServerNIO;

public class NIOTest {
	public static void main(String[] args) throws InterruptedException {
		new Thread(new ServerNIO(MyConstants.PORT_NIO)).start();
		clientNIOSend();
	}

	
	private static void clientNIOSend() throws InterruptedException {
		Thread.sleep(1000L);
		ClientNIO.client();
		Thread.sleep(1000L);
		ClientNIO.client();
		Thread.sleep(1000L);
		ClientNIO.client();
	}
}
