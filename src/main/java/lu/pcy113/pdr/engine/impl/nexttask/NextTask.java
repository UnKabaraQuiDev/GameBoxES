package lu.pcy113.pdr.engine.impl.nexttask;

public class NextTask {
	
	private int state = -1;
	
	private NextTaskFunction function;
	private NextTask callback;
	
	private final NextTaskEnvironnment sourceEnv, targetEnv;
	private final int source, target;
	
	public NextTask(int source, int target, NextTaskEnvironnment sourceEnv, NextTaskEnvironnment targetEnv) {
		this.source = source;
		this.target = target;
		this.sourceEnv = sourceEnv;
		this.targetEnv = targetEnv;
	}
	
	public NextTask exec(NextTaskFunction function) {
		this.function = function;
		return this;
	}
	
	public NextTask thenTask(NextTask nt) {
		this.callback = nt;
		return this;
	}
	
	public NextTask then(NextTaskFunction callback) {
		return thenTask(new NextTask(target, source, targetEnv, sourceEnv).exec(callback));
	}
	
	public boolean push() {
		return push(targetEnv);
	}
	
	public boolean push(NextTaskEnvironnment env) {
		if(env instanceof NextTaskWorker)
			return ((NextTaskWorker) env).push(this);
		
		return env.push(target, this);
	}
	
	public void execute() {
		this.execute(sourceEnv);
	}
	
	public void execute(NextTaskEnvironnment callbackTo) {
		this.state = this.function.run(state);
		
		if (this.callback != null) {
			this.callback.state = this.state;
			this.callback.push(callbackTo);
		}
	}
	
	public int getState() {
		return state;
	}
	
	public int getSource() {
		return source;
	}
	
	public int getTarget() {
		return target;
	}
	
}
