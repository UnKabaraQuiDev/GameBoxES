package lu.pcy113.pdr.engine.anim.skeletal;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/** 
 * @author Karl
 */

public class JointTransform {

	// remember, this position and rotation are relative to the parent bone!
	private final Vector3f position;
	private final Quaternionf rotation;

	public JointTransform(Vector3f position, Quaternionf rotation) {
		this.position = position;
		this.rotation = rotation;
	}

	protected Matrix4f getLocalTransform() {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(position);
		matrix.mul(new Matrix4f().rotate(rotation));
		return matrix;
	}

	protected static JointTransform interpolate(JointTransform frameA, JointTransform frameB, float progression) {
		Vector3f pos = interpolate(frameA.position, frameB.position, progression);
		Quaternionf rot = new Quaternionf(frameA.rotation).slerp(frameB.rotation, progression);
		return new JointTransform(pos, rot);
	}

	private static Vector3f interpolate(Vector3f start, Vector3f end, float progression) {
		float x = start.x + (end.x - start.x) * progression;
		float y = start.y + (end.y - start.y) * progression;
		float z = start.z + (end.z - start.z) * progression;
		return new Vector3f(x, y, z);
	}

}
