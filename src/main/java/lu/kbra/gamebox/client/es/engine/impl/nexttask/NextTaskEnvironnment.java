package lu.kbra.gamebox.client.es.engine.impl.nexttask;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NextTaskEnvironnment {

	/**
	 * blocks push of next task
	 */
	private boolean inputClosed = false;
	/**
	 * blocks retrieving of next task
	 */
	private boolean blocking = false;

	protected Thread[] threads;
	protected HashMap<Integer, Queue<NextTask>> queues = new HashMap<>();

	public NextTaskEnvironnment(int count) {
		for (int i = 0; i < count; i++) {
			queues.put(i, new ConcurrentLinkedQueue<>());
		}
	}

	public NextTaskEnvironnment(Thread[] threads) {
		this.threads = threads;

		for (int i = 0; i < threads.length; i++) {
			queues.put(i, new ConcurrentLinkedQueue<>());
		}
	}

	public Queue<NextTask> getQueue(int id) {
		return queues.get(id);
	}

	public <I, B, C> NextTask<I, B, C> getNext(int id) {
		if (blocking)
			return null;

		return queues.get(id).peek();
	}

	public void removeNext(int id) {
		if (blocking)
			return;

		queues.get(id).poll();
	}

	public boolean hasNext(int id) {
		if (blocking)
			return false;

		return !queues.get(id).isEmpty();
	}

	public boolean push(int id, NextTask task) {
		if (inputClosed)
			return false;

		boolean b = queues.get(id).add(task);

		wakeup(id);

		return b;
	}

	public void wakeup(int id) {
		if (threads == null || id >= threads.length || id < 0)
			return;

		Thread th = threads[id];
		if (th instanceof NextTaskThread) {
			((NextTaskThread) th).wakeUp();
		}
	}

	public int getShortestQueueId() {
		int thId = (int) (Math.random() * threads.length);
		int lowestValue = queues.get(thId).size();

		if (lowestValue == 0)
			return thId;

		for (Entry<Integer, Queue<NextTask>> entry : queues.entrySet()) {
			if (entry.getValue().size() == 0) {
				thId = entry.getKey();
				break;
			}

			if (entry.getValue().size() < lowestValue) {
				lowestValue = entry.getValue().size();
				thId = entry.getKey();
			}
		}

		return thId;
	}

	public void clearQueues() {
		getQueues().values().forEach(t -> t.clear());
	}
	
	public Thread[] getThreads() {
		return threads;
	}

	public void setThreads(Thread[] threads) {
		this.threads = threads;
	}

	public HashMap<Integer, Queue<NextTask>> getQueues() {
		return queues;
	}

	public boolean isInputClosed() {
		return inputClosed;
	}

	public void closeInput() {
		inputClosed = true;
	}

	public void openInput() {
		inputClosed = false;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public void block() {
		blocking = true;
	}

}
