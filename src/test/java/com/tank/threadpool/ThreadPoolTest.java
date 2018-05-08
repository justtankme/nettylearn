package com.tank.threadpool;

import java.time.Instant;

import com.tank.threadpool.MyThreadPool;
import com.tank.threadpool.MyThreadPoolImpl;

public class ThreadPoolTest {
	public static void main(String[] args) {
		MyThreadPool myThreadPool = new MyThreadPoolImpl();
		//当线程数小于核心线程数时，创建线程
		myThreadPool.execute(getTask(1));
		//当线程数大于等于核心线程数，且任务队列未满时，将任务放入任务队列。
		myThreadPool.execute(getTask(2));
		//当线程数大于等于核心线程数，且任务队列已满,若线程数小于最大线程数，创建线程
		myThreadPool.execute(getTask(3));
		//当线程数大于等于核心线程数，且任务队列已满,若线程数等于最大线程数，调用MyRejectPolicy
		myThreadPool.execute(getTask(4));
		myThreadPool.execute(getTask(5));
		myThreadPool.execute(getTask(6));
		myThreadPool.execute(getTask(7));
		myThreadPool.execute(getTask(8));
		myThreadPool.destory();
	}

	private static Runnable getTask(int i) {
		return new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println(i + " thread started");
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(i + " " + Instant.now().toString());
			}
		};
	}
}
