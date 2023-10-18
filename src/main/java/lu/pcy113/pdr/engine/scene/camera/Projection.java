package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;

public class Projection {
	
	protected Matrix4f projMatrix;
	protected float fov;
	protected float near, far;
	
	public Projection(float fov, float near, float far) {
		this.fov = fov;
		this.near = near;
		this.far = far;
		
		this.projMatrix = new Matrix4f();
	}
	
	public Matrix4f perspectiveUpdateMatrix(int width, int height) {
		return projMatrix.setPerspective(fov, (float) width / height, near, far);
	}
	public Matrix4f orthographicUpdateMatrix(int width, int height) {
		return projMatrix.setOrthoSymmetric(width, height, near, far);
	}
	
	public Matrix4f getProjMatrix() {return projMatrix;}
	public void setProjMatrix(Matrix4f projMatrix) {this.projMatrix = projMatrix;}
	
	public float getFar() {return far;}
	public void setFar(float far) {this.far = far;}
	public float getFov() {return fov;}
	public void setFov(float fov) {this.fov = fov;}
	public float getNear() {return near;}
	public void setNear(float near) {this.near = near;}
	
}
