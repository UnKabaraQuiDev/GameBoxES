package lu.pcy113.pdr.engine.impl.nexttask;

public class NextTask {
	
	private int state = -1;
	
	private NextTaskFunction function;
	private NextTask callback;
	
	private final NextTaskEnvironnment env;
	private final int source, target;
	
	public NextTask(int source, int target, NextTaskEnvironnment env) {
		this.source = source;
		this.target = target;
		this.env = env;
	}
	
	public NextTask exec(NextTaskFunction function) {
		this.function = function;
		return this;
	}
	
	public NextTask then(NextTaskFunction callback) {
		this.callback = new NextTask(target, source, env).exec(callback);
		return this;
	}
	
	public boolean push() {
		return push(env);
	}
	public boolean push(NextTaskEnvironnment env) {
		return env.push(target, this);
	}
	
	public void execute(NextTaskEnvironnment env) {
		this.state = this.function.run(state);
		
		if(this.callback != null) {
			this.callback.state = this.state;
			env.push(this.source, this.callback);
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
