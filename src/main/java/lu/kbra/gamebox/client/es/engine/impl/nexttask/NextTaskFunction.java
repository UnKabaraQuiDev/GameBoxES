package lu.kbra.gamebox.client.es.engine.impl.nexttask;

@FunctionalInterface
public interface NextTaskFunction<I, B> {

	B run(I state);

}
