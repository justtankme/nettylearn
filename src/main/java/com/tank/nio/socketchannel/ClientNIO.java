package com.tank.nio.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

import com.tank.constant.MyConstants;

/**
 * 客户端采用NIO实现
 * 
 * @author duanzhiwei
 *
 */
public class ClientNIO {
	public static void client() {
		//创建一个ByteBuffer对象，position=0/limit=capacity=1024/mark=null
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try(SocketChannel socketChannel = SocketChannel.open()) {
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(MyConstants.HOST, MyConstants.PORT_NIO));
			
			System.out.println(socketChannel.finishConnect());
			if (socketChannel.finishConnect()) {
				TimeUnit.SECONDS.sleep(1);
				String info = "I'm information from " + Thread.currentThread().getName();
				buffer.clear();
				buffer.put(info.getBytes());
				buffer.flip();
				while (buffer.hasRemaining()) {
					System.out.println(Thread.currentThread().getName() + " " + buffer);
					socketChannel.write(buffer);
				}
			}
			socketChannel.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
