package lu.pcy113.pdr.engine.impl.nexttask;

public class NextTaskThread extends Thread {

	public static final long MAX_SLEEP = Long.MAX_VALUE;

	private final int id;
	private final NextTaskEnvironnment env;

	private boolean running = true, sleeping = false;

	public NextTaskThread(int id, ThreadGroup threadGroup, NextTaskEnvironnment env) {
		super(threadGroup, threadGroup.getName() + "-" + id);

		this.id = id;
		this.env = env;
	}

	@Override
	public void run() {
		while (running) {
			exec: {
				if (env.hasNext(
						id)) {
					NextTask nt = env.getNext(
							id);
					nt.execute();
				}
			}

			if (env.hasNext(
					id))
				continue;

			waiting: {
				try {
					sleeping = true;
					Thread.sleep(
							MAX_SLEEP);
					if (!Thread.interrupted()) {
						// do nothing
					}
				} catch (InterruptedException e) {
					this.interrupt(); // restore state
				}
				sleeping = false;
			}

			/*
			 * if(!running) {
			 * this.interrupt(); // restore state
			 * } // else do nothing
			 */
		}
	}

	public void wakeUp() {
		if (sleeping) {
			this.interrupt();
		} // else will normally run in while
	}

	public void shutdown() {
		running = false;
		this.interrupt();
	}

	public NextTaskEnvironnment getEnv() {
		return env;
	}

}
