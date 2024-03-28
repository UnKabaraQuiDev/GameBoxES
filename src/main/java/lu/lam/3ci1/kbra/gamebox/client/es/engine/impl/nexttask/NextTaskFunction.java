package lu.pcy113.pdr.engine.impl.nexttask;

@FunctionalInterface
public interface NextTaskFunction<I, B> {

	B run(I state);

}
