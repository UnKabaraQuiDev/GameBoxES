package lu.pcy113.pdr.engine.impl.nexttask;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NextTaskEnvironnment {

	private boolean inputClosed = false, blocking = false;

	protected HashMap<Integer, Queue<NextTask>> queues = new HashMap<>();

	public NextTaskEnvironnment(int count) {
		for (int i = 0; i < count; i++) {
			queues.put(
					i,
					new ConcurrentLinkedQueue<>());
		}
	}

	public Queue<NextTask> getQueue(int id) {
		return queues.get(
				id);
	}

	public NextTask getNext(int id) {
		if (blocking)
			return null;

		return queues.get(
				id).poll();
	}

	public boolean hasNext(int id) {
		if (blocking)
			return false;

		return !queues.get(
				id).isEmpty();
	}

	public boolean push(int id, NextTask task) {
		if (inputClosed)
			return false;

		return queues.get(
				id).add(
						task);
	}

	public int getShortestQueueId() {
		int lowestValue = Integer.MAX_VALUE, thId = -1;
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

	public boolean isInputClosed() {
		return inputClosed;
	}

	public void closeInput() {
		inputClosed = false;
	}

	public void openInput() {
		inputClosed = true;
	}

	public boolean isBlocking() {
		return blocking;
	}

	public void block() {
		blocking = true;
	}

}
