
package server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread extends Thread {
	//comsumer
	//Declare blocking queue
	private BlockingQueue taskQueue;
	private boolean isStopped = false;
	private final ExecutorService pool = Executors.newFixedThreadPool(100);
	/**
	 * Constructor for the PoolThread
	 * @param queue
	 */
	public ServerThread(BlockingQueue queue) {
		taskQueue = queue;
	}
	
	/**
	 * Extracts the threads from the queue to execute a runnable
	 */
	public void run() {
		while (!isStopped()) {
			try {
				Runnable runnable = (Runnable) taskQueue.dequeue();
				pool.submit(runnable);
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
		// break pool thread out of dequeue() call.
		this.interrupt();
	}
	/*
	 * Boolean to check if the thread is stopped
	 */
	public synchronized boolean isStopped() {
		return isStopped;
	}
}
