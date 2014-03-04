package com.hiveview.domybox.common.image;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DownLoadTaskManager {
	
	private static final int SIZE = 10;
	
	private static ExecutorService POOL = Executors.newFixedThreadPool(SIZE);
	private static DownLoadTaskManager MANAGER = new DownLoadTaskManager();
	
	public static DownLoadTaskManager getInstance(){
		
		if(MANAGER.isShutdown()){
			POOL = null;
			POOL = Executors.newFixedThreadPool(SIZE);
		}
		
		return MANAGER;
	}
	
	public void submit(Runnable obj){
		
		POOL.submit(obj);
	}
	
	public void submit(Callable<?> obj){
		
		POOL.submit(obj);
		
	}
	
	public void shutdown(){
		
		POOL.shutdown();
		
	}
	
	public boolean isShutdown(){
		
		return POOL.isShutdown();
		
	}
	
	public boolean isTerminated(){
		
		return POOL.isTerminated();
		
	}
	
	public boolean awaitTermination(long timeOut,TimeUnit unit) throws InterruptedException{
		
		return POOL.awaitTermination(timeOut, unit);
		
	}
	
	public void shutDownNow(){
		POOL.shutdownNow();
		
		
		
		
		getInstance();
	}
	
}
