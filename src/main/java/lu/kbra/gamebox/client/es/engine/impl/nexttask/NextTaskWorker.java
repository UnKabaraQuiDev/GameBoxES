package lu.kbra.gamebox.client.es.engine.impl.nexttask;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.impl.UniqueID;

public class NextTaskWorker extends NextTaskEnvironnment implements UniqueID {

	private ThreadGroup threadGroup;

	public NextTaskWorker(String name, int workerCount) {
		super(workerCount);

		super.threads = new NextTaskThread[workerCount];
		this.threadGroup = new ThreadGroup(name);
		for (int i = 0; i < workerCount; i++) {
			NextTaskThread th = new NextTaskThread(i, this.threadGroup, this);
			super.threads[i] = th;
			th.start();
		}
	}

	public boolean push(NextTask... tasks) {
		boolean bb = true;

		for (NextTask nt : tasks) {
			bb &= push(nt);
		}

		return bb;
	}

	public boolean push(NextTask task) {
		int ntth = super.getShortestQueueId();

		boolean bb = super.push(ntth, task);

		GlobalLogger.info("Pushed task: " + task.getSource() + " -> " + task.getTarget() + " on thread: " + ntth + " = " + bb);

		((NextTaskThread) super.threads[ntth]).wakeUp();

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
			if(this.threads[i] != null) {
				((NextTaskThread) this.threads[i]).shutdown();
			}
		}
		for (int i = 0; i < threads.length; i++) {
			if(threads[i] == null)
				continue;
			
			this.threads[i].join();
			this.threads[i] = null;
		}
		
		if(this.threadGroup.activeCount() == 0) {
			this.threadGroup.destroy();
		}
	}

	public boolean isActive() {
		return threads.length != 0 && !threadGroup.isDestroyed();
	}

}
