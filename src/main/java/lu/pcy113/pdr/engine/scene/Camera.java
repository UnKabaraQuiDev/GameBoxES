package lu.pcy113.pdr.engine.scene;

import lombok.Getter;
import lombok.Setter;

public class Camera {
	
	@Getter @Setter
	private Transform transform;
	@Getter @Setter
	private Projection projection;
	
	public Camera(Transform trans, Projection proj) {
		this.transform = trans;
		this.projection = proj;
	}
	
}
