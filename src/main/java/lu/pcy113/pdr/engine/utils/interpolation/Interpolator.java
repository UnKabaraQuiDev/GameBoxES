package lu.pcy113.pdr.engine.utils.interpolation;

@FunctionalInterface
public interface Interpolator {

	float evaluate(float progress);

}
