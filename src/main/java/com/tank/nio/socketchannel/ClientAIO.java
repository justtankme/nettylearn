package com.tank.nio.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import com.tank.nio.constant.MyConstants;

public class ClientAIO implements Runnable {

	private AsynchronousSocketChannel channel;

	public ClientAIO() throws IOException {
	        channel = AsynchronousSocketChannel.open();
	    }

	public void connect() {
		channel.connect(new InetSocketAddress(MyConstants.HOST, MyConstants.PORT_AIO));
	}

	public void write(String data) {
		try {
			channel.write(ByteBuffer.wrap(data.getBytes())).get();
			read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void read() {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			channel.read(buffer).get();
			buffer.flip();
			byte[] bytes = new byte[buffer.remaining()];
			buffer.get(bytes);
			String data = new String(bytes, "UTF-8").trim();
			System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {

		}
	}
}
