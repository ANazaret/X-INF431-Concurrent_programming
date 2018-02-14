package data;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingBoundedQueue implements MessageQueue {

	private final Message[] queue;
    private final int bound;
    private int in, size;
    
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
	
	public BlockingBoundedQueue(int max) {
		this.queue = new Message[max];
        this.bound = max;
        this.in = 0;
        this.size = 0;
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
	}
	
	public boolean isFull() {
        return this.size >= this.bound;
    }
	
	@Override
	public boolean add(Message msg) {
		lock.lock();
		try {
			while (isFull()) {
				notFull.awaitUninterruptibly();
			}
			
			queue[in] = msg;
			in = (in + 1) % bound;
			++size;
			notEmpty.signalAll();
		}
		finally {
			lock.unlock();
		}
		
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public Message remove() {
		lock.lock();
		Message msg = null;
		
		try {
			while (isEmpty()) {
				notEmpty.awaitUninterruptibly();
			}
			
			int out = (in - size + bound) % bound;
			msg = queue[out];
			queue[out] = null;
			--size;
			notFull.signalAll();
			
		} finally {
			lock.unlock();
		}
		
		return msg;
	}
	

}
