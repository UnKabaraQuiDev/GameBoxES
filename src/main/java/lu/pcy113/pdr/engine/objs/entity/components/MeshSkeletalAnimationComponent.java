package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.anim.skeletal.MeshSkeletalAnimation;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class MeshSkeletalAnimationComponent extends Component {
	
	private MeshSkeletalAnimation meshSkeletalAnimation;

	public MeshSkeletalAnimationComponent(MeshSkeletalAnimation mesh) {
		this.meshSkeletalAnimation = mesh;
	}
	
	public MeshSkeletalAnimation getMeshSkeletalAnimation() {
		return meshSkeletalAnimation;
	}
	
	public void setMeshSkeletalAnimation(MeshSkeletalAnimation meshSkeletalAnimation) {
		this.meshSkeletalAnimation = meshSkeletalAnimation;
	}
	
}
