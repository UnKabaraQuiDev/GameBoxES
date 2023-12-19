package lu.pcy113.pdr.engine.utils.interpol;

@FunctionalInterface
public interface Interpolator {

	float evaluate(float progress);

}
