package com.tank.threadpool;

public interface MyThreadPool {
	public void execute(Runnable task);

	void destory();
}
