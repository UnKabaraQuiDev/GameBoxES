package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.utils.transform.Transform;
import lu.pcy113.pdr.engine.utils.transform.Transform2D;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class UIComponentRectangle extends UIComponent {

	// from center
	private Vector2f size;
	
	private boolean inheritScale;
	
	private Class<? extends TransformComponent> targetClazz;

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

}
