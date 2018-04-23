package com.tank.nio;

import com.tank.nio.constant.MyConstants;
import com.tank.nio.socketchannel.ClientAIO;
import com.tank.nio.socketchannel.ServerAIO;

public class AIOTest {
	public static void main(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new ServerAIO(MyConstants.PORT_AIO);
			}
		}).start();
		Thread.sleep(1000);
		clientAIOSend();
	}

	private static void clientAIOSend() throws InterruptedException {
		try {
			ClientAIO c1 = new ClientAIO();
			ClientAIO c2 = new ClientAIO();
			ClientAIO c3 = new ClientAIO();

			c1.connect();
			c2.connect();
			c3.connect();

			new Thread(c1).start();
			new Thread(c2).start();
			new Thread(c3).start();

			Thread.sleep(1000);

			c1.write("c1 aaa");
			c2.write("c2 bbbb");
			c3.write("c3 ccccc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
