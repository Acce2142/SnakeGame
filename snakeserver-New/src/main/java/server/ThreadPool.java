
package server;

import java.util.ArrayList;
import java.util.List;


public class ThreadPool {
	
	//Declare blocking queue
	private Buffer taskQueue = null;
	private List<ServerThread> threads = new ArrayList<ServerThread>();
	private boolean isStopped = false;
	
	/**
	 * Constructor for the threadPool. Passes through the number of
	 * threads used and the max amount of tasks
	 * @param threadNumer
	 * @param maxTasks
	 */
	public ThreadPool(int threadNumer, int maxTasks) {
		taskQueue = new Buffer(maxTasks);
		
		for (int i = 0; i < threadNumer; i++) {
			threads.add(new ServerThread(taskQueue));
		}
		for (ServerThread thread : threads) {
			thread.start();
		}
	}
	/**
	 * Executes task if threads are full the task is placed onto the blocking queue
	 * @param task
	 * @throws Exception
	 */
	public synchronized void execute(Runnable task) throws Exception {
		if (this.isStopped) throw new IllegalStateException("ThreadPool is stopped");
		this.taskQueue.put(task);
	}
	
	/**
	 * Stops the threads
	 */
	public synchronized void stop() {
		this.isStopped = true;
		for (ServerThread thread : threads) {
			thread.doStop();
		}
	}
	
}
