package lu.kbra.gamebox.client.es.game.game.utils;

import org.joml.Quaternionf;

import lu.kbra.gamebox.client.es.engine.anim.CallbackValueInterpolation;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolator;

public class QuaternionArrayCallbackValueInterpolator extends CallbackValueInterpolation<Entity[], Quaternionf> {

	public QuaternionArrayCallbackValueInterpolator(Entity[] object, Quaternionf start, Quaternionf end, Interpolator interpolator) {
		super(object, start, end, interpolator);
	}

	@Override
	public Quaternionf evaluate(float progress) {
		return start.slerp(end, progress, new Quaternionf());
	}

	@Override
	public void callback(Entity[] object, Quaternionf value) {
		for(Entity e : object) {
			if(e.hasComponent(Transform3DComponent.class)) {
				e.getComponent(Transform3DComponent.class).getTransform().setRotation(new Quaternionf(value)).updateMatrix();
			}
		}
	}

}
