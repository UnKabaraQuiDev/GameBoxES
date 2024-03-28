package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import lu.kbra.gamebox.client.es.engine.anim.skeletal.ArmatureAnimation;
import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public class ArmatureAnimationComponent extends Component {

	private ArmatureAnimation armatureAnimation;

	public ArmatureAnimationComponent(ArmatureAnimation mesh) {
		this.armatureAnimation = mesh;
	}

	public ArmatureAnimation getArmatureAnimation() {
		return armatureAnimation;
	}

	public void setArmatureAnimation(ArmatureAnimation armatureAnimation) {
		this.armatureAnimation = armatureAnimation;
	}

}
