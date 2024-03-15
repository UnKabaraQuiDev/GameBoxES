package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.audio.Sound;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class Sound3DComponent extends Component {

	private Sound sound;

	public Sound3DComponent(Sound sound) {
		this.sound = sound;
	}

	public void update() {
		updatePositionDirection();
		updateVelocity();
	}

	public void updateVelocity() {
		// TODO: Implement velocity component
	}

	public void updatePositionDirection() {
		if (!super.getParent().hasComponent(Transform3DComponent.class))
			return;

		Transform3D transform = super.getParent().getComponent(Transform3DComponent.class).getTransform();
		
		sound.setPosition(transform.getTranslation());
		System.err.println("sound position: " + transform.getTranslation());
		sound.setDirection(new Vector3f(0, 0, 1).rotate(transform.getRotation()));
		System.err.println("sound direction: " + new Vector3f(0, 0, 1).rotate(transform.getRotation()));
	}

	public Sound getSound() {
		return sound;
	}

	public void setSound(Sound sound) {
		this.sound = sound;
	}

}
