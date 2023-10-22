package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;

public class Projection {
	
	protected Matrix4f projMatrix;
	protected float near, far;
	protected boolean perspective = true;
	
	// perspective only
	protected float fov;
	
	// orthographic only
	protected float size = 0.8f;
	protected float left, right, bottom, top;
	
	protected int width, height;
	
	public Projection(boolean perspective, float fov_size, float near, float far) {
		this.fov = fov_size;
		this.size = fov_size;
		this.near = near;
		this.far = far;
		
		this.projMatrix = new Matrix4f();
	}
	public Projection(float near, float far, float left, float right, float bottom, float top) {
		this(near, far, left, right, bottom, top, 0.8f*150);
	}
	public Projection(float near, float far, float left, float right, float bottom, float top, float size) {
		this.perspective = false;
		this.near = near;
		this.far = far;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.size = size;
		
		this.projMatrix = new Matrix4f();
		
		update();
	}
	
	public Matrix4f perspectiveUpdateMatrix(int width, int height) {
		return projMatrix.setPerspective(fov, (float) width / height, near, far);
	}
	public Matrix4f orthographicUpdateMatrix(int width, int height) {
		return projMatrix.setOrthoSymmetric(width/size, height/size, near, far);
		//return projMatrix.ortho(left*width/size, right*width/size, bottom*height/size, top*height/size, near, far);
	}
	
	public Matrix4f update(int w, int h) {
		this.width = w;
		this.height = h;
		if(perspective) {
			return perspectiveUpdateMatrix(w, h);
		}else {
			return orthographicUpdateMatrix(w, h);
		}
	}
	public void update() {
		update(width, height);
	}
	
	public Matrix4f getProjMatrix() {return projMatrix;}
	public void setProjMatrix(Matrix4f projMatrix) {this.projMatrix = projMatrix;}
	
	public float getFar() {return far;}
	public void setFar(float far) {this.far = far;}
	public float getFov() {return fov;}
	public void setFov(float fov) {this.fov = fov;}
	public float getNear() {return near;}
	public void setNear(float near) {this.near = near;}
	public boolean isPerspective() {return perspective;}
	public boolean isOrthographic() {return !perspective;}
	public void setPerspective(boolean perspective) {this.perspective = perspective;}
	public float getSize() {return size;}
	public void setSize(float size) {this.size = size;}

}
