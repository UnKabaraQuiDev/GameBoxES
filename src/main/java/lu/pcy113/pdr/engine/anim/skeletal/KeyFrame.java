package lu.pcy113.pdr.engine.anim.skeletal;

import java.util.Map;

/**
 * @author Karl
 */
public class KeyFrame {

	private final float timeStamp;
	private final Map<String, JointTransform> pose;

	public KeyFrame(float timeStamp, Map<String, JointTransform> jointKeyFrames) {
		this.timeStamp = timeStamp;
		this.pose = jointKeyFrames;
	}

	protected float getTimeStamp() {
		return timeStamp;
	}
	
	protected Map<String, JointTransform> getJointKeyFrames() {
		return pose;
	}

}
