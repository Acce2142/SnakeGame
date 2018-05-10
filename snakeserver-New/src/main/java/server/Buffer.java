package server;

public class Buffer {
	// Size of the buffer
	private int size;
	// The buffer is implemented as an array
	private int[] buffer;
	// The pointers to the append and take positions
	private int InPtr = 0, OutPtr = 0;
	// The number of items in the buffer
	private int Count = 0;
	
	// Constructor takes the size as a parameter
	public Buffer(int size) {
		this.size = size;
		buffer = new int[size];
	}
	
	// we need to make sure only one thread is appending at a time
	// so we use the built in monitor
	public synchronized void append(int value) throws InterruptedException {
		// If the buffer is full we cannot append to it
		if (Count == size) {
			
		} else {
			// Place the value in the buffer
			buffer[InPtr] = value;
			InPtr = (InPtr + 1) % size;
			// Update the count
			Count = Count + 1;
			// If this is the first item added will the consumer thread know?
			this.notifyAll(); // } closing brace no longer needed
		}
	}
	
	public synchronized int take() {
		if (Count == 0) {
			return 0;
		} else {
			int I = buffer[OutPtr];
			OutPtr = (OutPtr + 1) % size;
			Count = Count - 1;
			notifyAll();
			return I;
		}
	}
}