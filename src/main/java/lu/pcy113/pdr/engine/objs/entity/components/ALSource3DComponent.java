package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.audio.ALSource;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class ALSource3DComponent extends Component {

	private ALSource source;

	public ALSource3DComponent(ALSource source) {
		this.source = source;
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

		source.setPosition(transform.getTranslation());
		//System.err.println("sound position: " + transform.getTranslation());
		source.setDirection(new Vector3f(0, 0, 1).rotate(transform.getRotation()));
		//System.err.println("sound direction: " + new Vector3f(0, 0, 1).rotate(transform.getRotation()));
	}

	public ALSource getSource() {
		return source;
	}

	public void setSource(ALSource source) {
		this.source = source;
	}

}
