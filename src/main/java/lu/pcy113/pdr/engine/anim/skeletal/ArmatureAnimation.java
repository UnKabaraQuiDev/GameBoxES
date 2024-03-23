package lu.pcy113.pdr.engine.anim.skeletal;

import org.joml.Matrix4f;

import lu.pcy113.pdr.engine.graph.shader.RenderShader;

/**
 * @author Original author: Karl
 */
public class ArmatureAnimation {

	public static final int MAX_WEIGHTS = 3;
	public static final String BONE_UNIFORM = "jointTransforms";

	// skeleton
	private final Bone rootBone;
	private final int boneCount;

	private final Animator animator;

	/**
	 * Creates a new entity capable of animation. The inverse bind transform for all
	 * joints is calculated in this constructor. The bind transform is simply the
	 * original (no pose applied) transform of a joint in relation to the model's
	 * origin (model-space). The inverse bind transform is simply that but inverted.
	 * 
	 * @param model - the VAO containing the mesh data for this entity. This
	 * includes vertex positions, normals, texture coords, IDs of joints that affect
	 * each vertex, and their corresponding weights.
	 * @param texture - the diffuse texture for the entity.
	 * @param rootJoint - the root joint of the joint hierarchy which makes up the
	 * "skeleton" of the entity.
	 * @param jointCount - the number of joints in the joint hierarchy (skeleton)
	 * for this entity.
	 * 
	 */
	public ArmatureAnimation(Bone rootJoint, int jointCount) {
		this.rootBone = rootJoint;
		this.boneCount = jointCount;
		this.animator = new Animator(this);
		rootJoint.calcInverseBindTransform(new Matrix4f());
	}

	public Bone getRootBone() {
		return rootBone;
	}

	public Animator getAnimator() {
		return animator;
	}

	public int getBoneCount() {
		return boneCount;
	}

	public void bind(RenderShader shader) {
		Matrix4f[] jointMatrices = getJointTransforms();
		for (int i = 0; i < boneCount; i++) {
			shader.setUniform(BONE_UNIFORM + "[" + i + "]", jointMatrices[i]);
			// System.out.println("Uniform: "+BONE_UNIFORM+"["+i+"] = "+jointMatrices[i]);
		}
	}

	/**
	 * Gets an array of the all important model-space transforms of all the joints
	 * (with the current animation pose applied) in the entity. The joints are
	 * ordered in the array based on their joint index. The position of each joint's
	 * transform in the array is equal to the joint's index.
	 * 
	 * @return The array of model-space transforms of the joints in the current
	 * animation pose.
	 */
	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[boneCount];
		addJointsToArray(rootBone, jointMatrices);
		return jointMatrices;
	}

	/**
	 * This adds the current model-space transform of a joint (and all of its
	 * descendants) into an array of transforms. The joint's transform is added into
	 * the array at the position equal to the joint's index.
	 * 
	 * @param headJoint - the current joint being added to the array. This method
	 * also adds the transforms of all the descendents of this joint too.
	 * @param jointMatrices - the array of joint transforms that is being filled.
	 */
	private void addJointsToArray(Bone headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
		for (Bone childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

}
