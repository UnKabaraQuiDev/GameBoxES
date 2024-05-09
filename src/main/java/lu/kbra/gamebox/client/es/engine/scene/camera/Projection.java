package lu.kbra.gamebox.client.es.engine.scene.camera;

import java.io.PrintStream;

import org.joml.Matrix4f;

public class Projection {

	private Matrix4f projectionMatrix;
	private float nearPlane, farPlane;
	private boolean perspective;
	private int width, height;
	private float fov, size = 100f;

	public void dump(PrintStream out) {
		out.println("Planes: "+nearPlane+" <> "+farPlane);
		out.println("Perspective: "+perspective);
		out.println("Resolution: "+width+":"+height);
		out.println("Fov: "+fov+", size: "+size);
	}
	
	public Projection(int width, int height, float nearPlane, float farPlane, float fov, float size, boolean perspective) {
		this.width = width;
		this.height = height;
		this.nearPlane = nearPlane;
		this.farPlane = farPlane;
		this.fov = fov;
		this.perspective = perspective;
		this.size = size;

		update();
	}

	public Projection update(int w, int h) {
		this.width = w;
		this.height = h;
		return update();
	}

	public Projection update() {
		if (perspective) {
			float aspectRatio = (float) width / height;
			float yScale = (float) (1 / Math.tan(Math.toRadians(fov / 2)));
			// float xScale = yScale / aspectRatio;
			// float frustumLength = farPlane - nearPlane;

			projectionMatrix = new Matrix4f().identity().perspective(fov, aspectRatio, nearPlane, farPlane);
		} else {
			float halfWidth = width / 2f;
			float halfHeight = height / 2f;

			projectionMatrix = new Matrix4f().identity().ortho(-halfWidth/size, halfWidth/size, -halfHeight/size, halfHeight/size, nearPlane, farPlane);
		}
		return this;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public Projection setPerspective(boolean perspective) {
		this.perspective = perspective;
		return this;
	}

	public Projection setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
		return this;
	}

	public Projection setFarPlane(float farPlane) {
		this.farPlane = farPlane;
		return this;
	}

	public Projection setFov(float fov) {
		this.fov = fov;
		return this;
	}

	public Projection setSize(int width, int height) {
		this.width = width;
		this.height = height;
		return this;
	}

	public Projection setSize(float size) {
		this.size = size;
		return this;
	}


}
