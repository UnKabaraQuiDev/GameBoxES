package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.anim.CallbackValueInterpolation;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.utils.MathUtils;
import lu.pcy113.pdr.engine.utils.interpolation.Interpolators;
import lu.pcy113.pdr.engine.utils.transform.Transform;
import lu.pcy113.pdr.engine.utils.transform.Transform2D;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class UIComponentRectangle extends UIComponent {

	// from center
	private Vector2f size;
	
	private boolean inheritScale;
	
	private Class<? extends TransformComponent> targetClazz;
	
	private CallbackValueInterpolation<Transform3D, Vector3f> interpolator;
	
	public UIComponentRectangle(Vector2f size, boolean inheritScale, Class<? extends TransformComponent> targetClazz) {
		this.size = size;
		this.targetClazz = targetClazz;
		this.inheritScale = inheritScale;
	}

	@Override
	public boolean contains(Vector2f point) {
		Transform tc = ((TransformComponent) super.getParent().getComponent(targetClazz)).getTransform();
		Vector2f center = new Vector2f(0), size = new Vector2f(this.size);
		
		if(tc instanceof Transform3D) {
			Vector3f translation = ((Transform3D) tc).getTranslation();
			center = new Vector2f(translation.x, translation.y);
			Vector3f scale = ((Transform3D) tc).getScale();
			size.mul(new Vector2f(scale.x, scale.y));
		} else if(tc instanceof Transform2D) {
			center = ((Transform2D) tc).getTranslation();
			size.mul(((Transform2D) tc).getScale());
		}
		
		//System.err.println("bounding box: "+center+" "+size);
		
		float minX = center.x - size.x / 2;
		float maxX = center.x + size.x / 2;
		float minY = center.y - size.y / 2;
		float maxY = center.y + size.y / 2;
		
		return point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY;
	}

	@Override
	public boolean attach(Entity parent) {
		boolean attached = super.attach(parent);
		if(!attached)
			return attached;
		
		interpolator = new CallbackValueInterpolation<Transform3D, Vector3f>(parent.getComponent(Transform3DComponent.class).getTransform(), new Vector3f(1), new Vector3f(2f), Interpolators.LINEAR) {
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
	
	@Override
	public void hover(Vector2f pos) {
		if(isReverse == true) {
			interpolator.progress = Interpolators.findClosestX(interpolator.getInterpolator().evaluate(interpolator.progress), Interpolators.BACK_OUT, 0.025f, 0);
			isReverse = false;
		}
		interpolator.setInterpolator(Interpolators.BACK_OUT);
		interpolator.add(0.1f).clamp().exec();
	}
	
	private boolean done = true;
	
	public boolean needsAttention() {
		return interpolator.progress != 0;
	}
	
	public void attention(Vector2f pos) {
		if(isReverse == false) {
			interpolator.progress = Interpolators.findClosestX(interpolator.getInterpolator().evaluate(interpolator.progress), Interpolators.QUAD_IN, 0.025f, 1);
			isReverse = true;
		}
		interpolator.setInterpolator(Interpolators.QUAD_IN);
		interpolator.add(-0.15f).clamp().exec();
	}

}
