package lu.pcy113.pdr.engine.impl.nexttask;

@FunctionalInterface
public interface NextTaskFunction {

	int run(int state);

}
