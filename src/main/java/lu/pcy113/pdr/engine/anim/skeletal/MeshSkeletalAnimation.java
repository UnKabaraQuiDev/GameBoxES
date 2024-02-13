package lu.pcy113.pdr.engine.anim.skeletal;

import org.joml.Matrix4f;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;

public class MeshSkeletalAnimation {
	
	private Joint root;
	private int jointCount;
	
	private Animator animator;
	
	public MeshSkeletalAnimation(Joint rootJoint, int jointCount) {
		this.root = rootJoint;
		this.jointCount = jointCount;
		this.animator = new Animator(this);
		this.root.calcInverseBindTransform(new Matrix4f());
	}
	
	public void doAnimation(Animation animation) {
		animator.doAnimation(animation);
	}
	
	public void update(float deltaTime) {
		animator.update(deltaTime);
	}
	
	private Matrix4f[] jointMatrices;
	
	public void bind(RenderShader shader) {
		if (jointMatrices == null) {
			jointMatrices = getJointTransforms();
		}
		GlobalLogger.log("Binding RenderShader joints: " + jointCount + " & " + jointMatrices);
		for (int index = 0; index < jointCount; index++) {
			shader.setUniform("jointTransforms[" + index + "]", jointMatrices[index]);
		}
	}
	
	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(root, jointMatrices);
		return jointMatrices;
	}
	
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
		for (Joint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}
	
	public Joint getRoot() {
		return root;
	}
	
	public int getJointCount() {
		return jointCount;
	}
	
	public Animator getAnimator() {
		return animator;
	}
	
}
