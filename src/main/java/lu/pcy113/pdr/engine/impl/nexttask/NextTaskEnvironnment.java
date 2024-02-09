package lu.pcy113.pdr.engine.impl.nexttask;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NextTaskEnvironnment {
	
	protected HashMap<Integer, Queue<NextTask>> queues = new HashMap<>();
	
	public NextTaskEnvironnment(int count) {
		for (int i = 0; i < count; i++) {
			queues.put(i, new ConcurrentLinkedQueue<>());
		}
	}
	
	public Queue<NextTask> getQueue(int id) {
		return queues.get(id);
	}
	
	public NextTask getNext(int id) {
		System.err.println(id + " get next : " + queues.get(id).peek());
		return queues.get(id).poll();
	}
	
	public boolean hasNext(int id) {
		System.err.println(id + " has next : " + !queues.get(id).isEmpty());
		return !queues.get(id).isEmpty();
	}
	
	public boolean push(int id, NextTask task) {
		System.err.println(task.getSource() + " pushed task onto " + id);
		return queues.get(id).add(task);
	}
	
}
