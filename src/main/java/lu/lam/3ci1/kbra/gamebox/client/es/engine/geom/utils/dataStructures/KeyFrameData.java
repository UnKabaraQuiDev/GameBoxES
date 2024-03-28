package lu.pcy113.pdr.engine.geom.utils.dataStructures;

import java.util.ArrayList;
import java.util.List;

public class KeyFrameData {

	public final float time;
	public final List<JointTransformData> jointTransforms = new ArrayList<JointTransformData>();

	public KeyFrameData(float time) {
		this.time = time;
	}

	public void addJointTransform(JointTransformData transform) {
		jointTransforms.add(transform);
	}

}
