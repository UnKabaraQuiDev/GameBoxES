package lu.pcy113.pdr.engine.scene;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Entity {
	
	private final String id, modelId;
	
	private Matrix4f modelMatrix;
	private Vector3f position;
	private Quaternionf rotation;
	private float scale;
	
	public Entity(String id, String modelId, Vector3f pos, Quaternionf rot, float scale) {
		this.id = id;
		this.modelId = modelId;
		this.modelMatrix = new Matrix4f();
		this.position = pos;
		this.rotation = rot;
		this.scale = scale;
		updateModelMatrix();
	}
	public Entity(String id, String modelId) {
		this(id, modelId, new Vector3f(), new Quaternionf(), 1);
	}
	
	public void updateModelMatrix() {
		modelMatrix.translationRotateScale(position, rotation, scale);
	}
	
	public String getId() {return id;}
	public String getModelId() {return modelId;}
	public Matrix4f getModelMatrix() {return modelMatrix;}
	public Vector3f getPosition() {return position;}
	public Quaternionf getRotation() {return rotation;}
	public float getScale() {return scale;}
	public void setPosition(float x, float y, float z) {position.set(x, x, z);}
	public void rotateAlongAxisDeg(float x, float y, float z, float a) {rotation.fromAxisAngleDeg(x, y, z, a);}
	public void rotateAlongAxisDeg(Vector3f v, float a) {rotation.fromAxisAngleDeg(v, a);}
	public void setScale(float scale) {this.scale = scale;}
	
}
