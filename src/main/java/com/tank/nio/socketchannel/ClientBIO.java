package com.tank.nio.socketchannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.tank.constant.MyConstants;

public class ClientBIO {

	public static void client() {
		try (Socket socket = new Socket(MyConstants.HOST, MyConstants.PORT_BIO);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);) {
			printWriter.println("客户端请求了服务器....");
			String response = bufferedReader.readLine();
			System.out.println("Client：" + response);
			socket.shutdownOutput();
			socket.shutdownInput();
			bufferedReader.close();
			printWriter.close();
			while (!socket.isClosed()) {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
