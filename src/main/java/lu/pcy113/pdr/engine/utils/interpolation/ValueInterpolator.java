package lu.pcy113.pdr.engine.utils.interpolation;

public interface ValueInterpolator<T> {

	T evaluate(T one, T two, float progress);

}
