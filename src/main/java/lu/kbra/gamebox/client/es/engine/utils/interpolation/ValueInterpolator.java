package lu.kbra.gamebox.client.es.engine.utils.interpolation;

public interface ValueInterpolator<T> {

	T evaluate(T one, T two, float progress);

}
