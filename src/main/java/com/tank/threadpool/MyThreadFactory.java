package com.tank.threadpool;

import java.util.concurrent.ThreadFactory;

public class MyThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		System.out.println("new Thread");
		return new Thread(r);
	}

}
