package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.anim.CallbackValueInterpolation;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolator;
import lu.kbra.gamebox.client.es.engine.utils.interpolation.Interpolators;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform2D;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;

public class UIComponentRectangleScale extends UIComponent {

	// from center
	private Vector2f size;

	private boolean inheritScale;

	private Class<? extends TransformComponent> targetClazz;

	private CallbackValueInterpolation<Transform3D, Vector3f> interpolator;

	public UIComponentRectangleScale(Vector2f size, boolean inheritScale, Class<? extends TransformComponent> targetClazz) {
		this.size = size;
		this.targetClazz = targetClazz;
		this.inheritScale = inheritScale;
	}

	@Override
	public boolean contains(Vector2f point) {
		Transform tc = ((TransformComponent) super.getParent().getComponent(targetClazz)).getTransform();
		Vector2f center = new Vector2f(0), size = new Vector2f(this.size);

		if (tc instanceof Transform3D) {
			Vector3f translation = ((Transform3D) tc).getTranslation();
			center = new Vector2f(translation.x, translation.y);
			Vector3f scale = ((Transform3D) tc).getScale();
			if(inheritScale)
				size.mul(new Vector2f(scale.x, scale.y));
		} else if (tc instanceof Transform2D) {
			center = ((Transform2D) tc).getTranslation();
			if(inheritScale)
				size.mul(((Transform2D) tc).getScale());
		}

		// System.err.println("bounding box: "+center+" "+size);

		float minX = center.x - size.x / 2;
		float maxX = center.x + size.x / 2;
		float minY = center.y - size.y / 2;
		float maxY = center.y + size.y / 2;

		return point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY;
	}

	@Override
	public boolean attach(Entity parent) {
		boolean attached = super.attach(parent);
		if (!attached)
			return attached;

		interpolator = new CallbackValueInterpolation<Transform3D, Vector3f>(parent.getComponent(Transform3DComponent.class).getTransform(), new Vector3f(1), new Vector3f(1.1f), Interpolators.LINEAR) {
			@Override
			public Vector3f evaluate(float pro) {
				return start.lerp(end, pro, new Vector3f());
			}

			@Override
			public void callback(Transform3D object, Vector3f value) {
				object.setScale(value).updateMatrix();
			}

		};
		return attached;
	}

	private boolean isReverse = false;

	private Interpolator outIntepolation = Interpolators.QUAD_OUT, inInterpolation = Interpolators.BACK_OUT;

	private float inSpeed = 0.18f, outSpeed = 0.25f;

	@Override
	public void hover(Vector2f pos) {
		if (isReverse == true) {
			interpolator.set(Interpolators.inverse(interpolator.getInterpolator().evaluate(interpolator.progress), inInterpolation, 0.025f, 0));
			isReverse = false;
		}
		interpolator.setInterpolator(inInterpolation);
		interpolator.add(inSpeed).clamp().exec();
	}

	public boolean needsAttention() {
		return interpolator.progress != 0;
	}

	public void attention(Vector2f pos) {
		if (isReverse == false) {
			interpolator.set(Interpolators.inverse(interpolator.getInterpolator().evaluate(interpolator.progress), outIntepolation, 0.025f, 1));
			isReverse = true;
		}
		interpolator.setInterpolator(Interpolators.QUAD_OUT);
		interpolator.add(-outSpeed).clamp().exec();
	}

}
