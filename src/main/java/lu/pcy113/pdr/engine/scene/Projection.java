package lu.pcy113.pdr.engine.scene;

import org.joml.Matrix4f;

import lombok.Getter;
import lombok.Setter;

public class Projection {
	
	@Getter @Setter
	private float fov;
	@Getter @Setter
	private float zNear, zFar;
	@Getter @Setter
	private Matrix4f matrix;
	
	public Projection(float fov, float near, float far) {
		this.fov = fov;
		this.zNear = near;
		this.zFar = far;
		
		matrix = new Matrix4f();
	}
	
	public Projection fov(float fov) {
		this.fov = fov;
		return this;
	}
	
	public Matrix4f updateMatrix(float w, float h) {
		matrix.setPerspective(fov, w/h, zNear, zFar);
		return matrix;
	}
	
}
