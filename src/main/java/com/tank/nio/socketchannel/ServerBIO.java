package com.tank.nio.socketchannel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.tank.nio.constant.MyConstants;

public class ServerBIO {
	public static void server() {
		try (ServerSocket serverSocket = new ServerSocket(MyConstants.PORT_BIO)) {
			ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10, 0, TimeUnit.SECONDS,
					new LinkedBlockingQueue<>());
			while (true) {
				Socket clntSocket = serverSocket.accept();
				executor.submit(new Runnable() {
					@Override
					public void run() {
						handleStream(clntSocket);
					}
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleStream(Socket clntSocket) {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clntSocket.getInputStream()));
				PrintWriter printWriter = new PrintWriter(clntSocket.getOutputStream(), true);) {
			while (true) {
				String info = bufferedReader.readLine();
				if (info == null)
					break;
				System.out.println("客户端发送的消息：" + info);
				printWriter.println("服务器端响应了客户端请求....");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
