
package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread extends Thread {
	//comsumer
	//Declare blocking queue
	private Buffer taskQueue;
	private boolean isStopped = false;
	/**
	 * Constructor for the PoolThread
	 * @param queue
	 */
	public ServerThread(Buffer queue) {
		taskQueue = queue;
	}
	
	/**
	 * Extracts the threads from the queue to execute a runnable
	 */
	public void run() {
		while (!isStopped()) {
			try {
				Runnable runnable = (Runnable) taskQueue.take();
				runnable.run();
			}
			catch (Exception e) {
				// log or otherwise report exception,
				// but keep pool thread alive.
			}
		}
	}
	
	/**
	 * Stops the threads
	 */
	public synchronized void doStop() {
		isStopped = true;
		
		this.interrupt();
	}
	/*
	 * Boolean to check if the thread is stopped
	 */
	public synchronized boolean isStopped() {
		return isStopped;
	}
}
