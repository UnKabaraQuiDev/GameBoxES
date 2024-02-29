package lu.pcy113.pdr.engine.impl.nexttask;

import lu.pcy113.pdr.engine.impl.UniqueID;

public class NextTaskWorker extends NextTaskEnvironnment implements UniqueID {

	private ThreadGroup threadGroup;
	private NextTaskThread[] threads;

	public NextTaskWorker(String name, int workerCount) {
		super(workerCount);

		this.threads = new NextTaskThread[workerCount];
		this.threadGroup = new ThreadGroup(
				name);
		for (int i = 0; i < workerCount; i++) {
			NextTaskThread th = new NextTaskThread(
					i,
					this.threadGroup,
					this);
			this.threads[i] = th;
			th.start();
		}
	}

	public boolean push(NextTask task) {
		int ntth = super.getShortestQueueId();

		boolean bb = super.push(
				ntth,
				task);

		this.threads[ntth].wakeUp();

		return bb;
	}

	@Override
	public String getId() {
		return threadGroup != null ? threadGroup.getName() : null;
	}

	/**
	 * Destroys all threads, lets the current task finish
	 * 
	 * @throws InterruptedException
	 */
	public void shutdown() throws InterruptedException {
		for (int i = 0; i < threads.length; i++) {
			this.threads[i].shutdown();
		}
		for (int i = 0; i < threads.length; i++) {
			this.threads[i].join();
			this.threads[i] = null;
		}

		this.threadGroup.destroy();
	}

	public boolean isActive() {
		return threads.length != 0 && !threadGroup.isDestroyed();
	}

}
