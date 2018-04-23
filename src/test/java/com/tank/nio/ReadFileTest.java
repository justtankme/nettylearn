package com.tank.nio;

import org.junit.Test;

import com.tank.nio.filechannel.ReadFile;

public class ReadFileTest {

	@Test
	public void test() {
		String filePath = "src/main/resources/normal_io.txt";
		ReadFile.readFileByInputStream(filePath);
		ReadFile.readFileByRandomAccess(filePath);
	}

}
