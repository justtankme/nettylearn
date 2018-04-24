package com.tank.threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MyThreadPoolImpl implements MyThreadPool {
	ThreadPoolExecutor executor = null;

	public MyThreadPoolImpl() {
		executor = new ThreadPoolExecutor(1, 3, 10, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(2), new MyThreadFactory(), new MyRejectPolicy());
	}

	@Override
	public void execute(Runnable task) {
		System.out.println(executor.getActiveCount());
		executor.execute(task);
	}
	
	@Override
	public void destory() {
		while(executor.getActiveCount() > 0) {
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("shuting down pool pool size " + executor.getPoolSize());
		System.out.println("shuting down pool max size " + executor.getMaximumPoolSize());
		executor.shutdown();
	}
}
