package com.tank.nio.filechannel;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReadFile {
	/**
	 * 使用inputstream读取文件
	 * @param filePath
	 */
	public static void readFileByInputStream(String filePath) {
		try (InputStream in = new BufferedInputStream(new FileInputStream(filePath))) {
			byte[] buf = new byte[1024];
			int bytesRead = in.read(buf);
			while (bytesRead != -1) {
				for (int i = 0; i < bytesRead; i++)
					System.out.print((char) buf[i]);
				bytesRead = in.read(buf);
			}
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用RandomAccessFile读取文件
	 * @param filePath
	 */
	public static void readFileByRandomAccess(String filePath) {
		try (RandomAccessFile aFile = new RandomAccessFile(filePath, "rw");) {
			FileChannel fileChannel = aFile.getChannel();
			ByteBuffer buf = ByteBuffer.allocate(1024);

			int bytesRead = fileChannel.read(buf);
			System.out.println(String.format("总共读取到了%d个字节",bytesRead));

			while (bytesRead != -1) {
				buf.flip();
				while (buf.hasRemaining()) {
					System.out.print((char) buf.get());
				}
				buf.compact();
				bytesRead = fileChannel.read(buf);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
