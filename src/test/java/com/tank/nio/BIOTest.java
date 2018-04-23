package com.tank.nio;

import com.tank.nio.socketchannel.ClientBIO;
import com.tank.nio.socketchannel.ServerBIO;

public class BIOTest {
	public static void main(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ServerBIO.server();
			}
		}).start();
		clientBIOSend();
	}

	private static void clientBIOSend() throws InterruptedException {
		Thread.sleep(1000L);
		ClientBIO.client();
		Thread.sleep(1000L);
		ClientBIO.client();
		Thread.sleep(1000L);
		ClientBIO.client();
	}
}
