package lu.pcy113.pdr.engine.anim.skeletal;

import java.util.Arrays;

import org.joml.Matrix4f;

import lu.pcy113.pdr.engine.utils.interpolation.Matrix4fValueInterpolator;

public class KeyFrame {
	
	private float moment;
	private Matrix4f[] transforms;
	
	public KeyFrame(float moment, Matrix4f[] transforms) {
		this.moment = moment;
	}
	
	public float getMoment() {
		return moment;
	}
	
	public Matrix4f[] getTransforms() {
		return transforms;
	}
	
	public KeyFrame getInterpolated(KeyFrame frame, Matrix4fValueInterpolator interpolator, float time) {
		time %= 1;
		
		KeyFrame inte = this.clone();
		for(int i = 0; i < inte.transforms.length; i++) {
			inte.transforms[i] = interpolator.evaluate(inte.transforms[i], frame.transforms[i], time);
		}
		
		return inte;
	}
	
	public KeyFrame clone() {
		return new KeyFrame(
				moment,
				Arrays.stream(transforms)
					.map(Matrix4f::new)
					.toArray(Matrix4f[]::new));
	}
	
}
