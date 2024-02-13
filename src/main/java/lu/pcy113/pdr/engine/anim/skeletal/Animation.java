package lu.pcy113.pdr.engine.anim.skeletal;

/**
* @author Karl
*/
public class Animation {
	
	private final float length;//in seconds
	private final KeyFrame[] keyFrames;

	public Animation(float lengthInSeconds, KeyFrame[] frames) {
		this.keyFrames = frames;
		this.length = lengthInSeconds;
	}

	public float getLength() {
		return length;
	}

	public KeyFrame[] getKeyFrames() {
		return keyFrames;
	}
	
}
