package lu.kbra.gamebox.client.es.engine.impl.nexttask;

public class NextTask<I, B, C> {

	private Object state;

	private NextTaskFunction<I, B> function;
	private NextTask<B, C, ?> callback;

	private final NextTaskEnvironnment sourceEnv, targetEnv;
	private final int source, target;

	public NextTask(int source, int target, NextTaskEnvironnment sourceEnv, NextTaskEnvironnment targetEnv) {
		this.source = source;
		this.target = target;
		this.sourceEnv = sourceEnv;
		this.targetEnv = targetEnv;
	}

	public NextTask<I, B, C> exec(NextTaskFunction<I, B> function) {
		this.function = function;
		return this;
	}

	public NextTask<I, B, C> thenTask(NextTask<B, C, ?> nt) {
		this.callback = nt;
		return this;
	}

	public <D> NextTask<I, B, C> then(NextTaskFunction<B, C> callback) {
		return thenTask(new NextTask<B, C, D>(target, source, targetEnv, sourceEnv).exec(callback));
	}

	public boolean push() {
		return push(targetEnv);
	}

	public boolean push(NextTaskEnvironnment env) {
		if (env instanceof NextTaskWorker)
			return ((NextTaskWorker) env).push(this);

		return env.push(target, this);
	}

	public void execute() {
		this.execute(sourceEnv);
	}

	public void execute(NextTaskEnvironnment callbackTo) {
		this.state = this.function.run((I) state);

		if (this.callback != null) {
			this.callback.state = this.state;
			this.callback.push(callbackTo);
		}
	}

	public NextTask<B, C, ?> getCallback() {
		return callback;
	}
	
	public Object getState() {
		return state;
	}

	public int getSource() {
		return source;
	}

	public int getTarget() {
		return target;
	}

}
