
package server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;


public class BlockingQueue {
	
	private Queue<Object> mQueue = new LinkedList<>();
	private int mLimit;
	private final AtomicInteger count = new AtomicInteger(0);
	
	public BlockingQueue(int limit) {
		mLimit = limit;
	}
	
	public synchronized void enqueue(Object item) throws InterruptedException {
		int oldCount = -1;
		while (count.get() == mLimit) wait();
		mQueue.add(item);
		oldCount = count.getAndIncrement();
		if (oldCount == 0) {
			notifyAll();
		}
	}
	
	public synchronized Object dequeue() throws InterruptedException {
		int oldCount = -1;
		while (count.get() == 0) wait();
		Object res = mQueue.remove();
		oldCount = count.getAndDecrement();
		if (oldCount == mLimit) {
			notifyAll();
		}
		return res;
	}
	
}
