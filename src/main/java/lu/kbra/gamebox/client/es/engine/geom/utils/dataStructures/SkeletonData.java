package lu.kbra.gamebox.client.es.engine.geom.utils.dataStructures;

public class SkeletonData {

	public final int jointCount;
	public final JointData headJoint;

	public SkeletonData(int jointCount, JointData headJoint) {
		this.jointCount = jointCount;
		this.headJoint = headJoint;
	}

}
