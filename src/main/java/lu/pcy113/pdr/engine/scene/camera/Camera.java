package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.GameEngine;

public abstract class Camera {
	
	protected Projection projection;
	
	protected Matrix4f viewMatrix;
	
	public Camera(Projection proj) {
		this.projection = proj;
		this.viewMatrix = new Matrix4f();
	}
	
	public abstract Matrix4f updateMatrix();
	
	public Matrix4f getViewMatrix() {return viewMatrix;}
	
	public Projection getProjection() {return projection;}
	public void setProjection(Projection projection) {this.projection = projection;}
	
	public static final Camera3D perspectiveCamera3D() {
		return new Camera3D(
				new Vector3f(), 
				new Quaternionf().identity().lookAlong(GameEngine.FORWARD.x, GameEngine.FORWARD.y, GameEngine.FORWARD.z, GameEngine.UP.x, GameEngine.UP.y, GameEngine.UP.z).rotateZ((float) Math.PI),
				//rotateXYZ((float) Math.toRadians(90), (float) Math.toRadians(180), 0),
				new Projection(true, (float) Math.toRadians(60), 0.01f, 1000f));
	}
	public static final Camera3D orthographicCamera3D() {
		return new Camera3D(
				new Vector3f(), 
				new Quaternionf().identity().lookAlong(GameEngine.FORWARD.x, GameEngine.FORWARD.y, GameEngine.FORWARD.z, GameEngine.UP.x, GameEngine.UP.y, GameEngine.UP.z).rotateZ((float) Math.PI),
				//rotateXYZ((float) Math.toRadians(90), (float) Math.toRadians(180), 0),
				new Projection(false, (float) Math.toRadians(60), 0.01f, 1000f));
	}
	
}
