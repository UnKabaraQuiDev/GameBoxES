package lu.kbra.gamebox.client.es.engine.utils.interpolation;

@FunctionalInterface
public interface Interpolator {

	float evaluate(float progress);

	default float inverse(float y) {
		return -1;
	}

}
