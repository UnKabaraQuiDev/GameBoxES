package lu.pcy113.pdr.engine.scene.camera;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Camera3D extends Camera {
	
	protected Vector3f position;
	//protected float pitch = 0, yaw = 0, roll = 0;
	protected Quaternionf rotation;
	
	protected final Vector3f UP = new Vector3f(0, 0, 1);
	
	public Camera3D(Vector3f position, Quaternionf rot, Projection proj) {
		super(proj);
		this.position = position;
		this.rotation = rot;
	}
	
	public void move(float ax, float ay, float bx, float by, float moveSpeed, float rotationSpeed) {
		// Move the camera based on the local camera axis
		Vector3f movement = new Vector3f(ax * moveSpeed, 0, -ay * moveSpeed);
		movement.rotate(rotation);
		
		// Update the camera's position
		position.add(movement);
		
		// Rotate the camera based on bx and by
		Quaternionf rotationChange = new Quaternionf().rotateYXZ(bx * rotationSpeed, by * rotationSpeed, 0);
		
		// Update the camera's rotation
		rotation.mul(rotationChange);
		
		/*// Calculate the rotation
		Quaternionf rotationChange = new Quaternionf().rotateYXZ(by * rotationSpeed, ax * rotationSpeed, 0);
		rotation.mul(rotationChange);
		
		// Calculate the movement
		Vector3f movement = new Vector3f(0, 0, -ay * moveSpeed);
		rotation.transform(movement);
		position.add(movement);*/
		
		/*Quaternionf pitch = new Quaternionf().rotateAxis(rotationSpeed*bx, UP);
		Quaternionf yaw = new Quaternionf().rotateXYZ(0, rotationSpeed*by, 0);
		Quaternionf cr = new Quaternionf().identity();
		cr.mul(yaw);
		cr.mul(pitch);
		rotation = cr;
		
		Vector3f moveDir = new Vector3f(ax, ay, 0).normalize();
		moveDir.mul(moveSpeed);
		position.add(moveDir);*/
		
		/*// Calculate camera rotation based on the bx and by joystick values.
		float pitch = by * rotationSpeed;
		float yaw = bx * rotationSpeed;
		Quaternionf rotationDelta = new Quaternionf().rotationYXZ(yaw, pitch, 0.0f);
		
		// Combine the current rotation with the rotation delta.
		rotation.mul(rotationDelta);
		
		// Calculate the camera's forward and right vectors in its local space.
		Vector3f forward = new Vector3f(0, 0, 1).rotate(rotation);
		Vector3f right = new Vector3f(1, 0, 0).rotate(rotation);
		
		// Calculate the movement in the camera's local space based on ax and ay joystick values.
		float moveX = ax * moveSpeed;
		float moveY = ay * moveSpeed;
		
		// Update the camera position.
		position.add(forward.mul(moveY));
		position.add(right.mul(moveX));*/
		
		/*// Calculate the forward direction
		Vector3f forward = new Vector3f(0, 0, -1);
		forward.rotate(rotation);
		
		// Calculate the right direction
		Vector3f right = new Vector3f(forward).cross(UP).normalize();
		
		// Move the camera
		position.add(new Vector3f(forward).mul(ax * moveSpeed));
		position.add(new Vector3f(right).mul(ay * moveSpeed));
		
		// Rotate the camera
		Quaternionf pitchRotation = new Quaternionf().rotateAxis(-bx * rotationSpeed, right);
		Quaternionf yawRotation = new Quaternionf().rotateAxis(by * rotationSpeed, UP);
		
		rotation.mul(pitchRotation);
		rotation.mul(yawRotation);*/
		
		/*// Calculate the camera's right and forward vectors
        Vector3f right = new Vector3f();
        Vector3f forward = new Vector3f();

        rotation.positiveX(right).cross(UP, forward);

        // Move the camera along its local axes
        position.add(right.mul(ax * moveSpeed));
        position.add(forward.mul(-ay * moveSpeed));

        // Rotate the camera around its local axes
        Quaternionf pitchChange = new Quaternionf().rotateAxis(-bx * rotationSpeed, right);
        Quaternionf yawChange = new Quaternionf().rotateAxis(by * rotationSpeed, UP);

        rotation.mul(pitchChange).mul(yawChange);*/
	}
	
	public Vector3f getPosition() {return position;}
	public Camera3D setPosition(Vector3f position) {this.position = position; return this;}
	public Quaternionf getRotation() {return rotation;}
	public void setRotation(Quaternionf rotation) {this.rotation = rotation;}
	
	public void rotate(float leftRight, float upDown) {
		// Rotate the camera using a quaternion
		rotation.mul(new Quaternionf().identity().rotateXYZ(-upDown, leftRight, 0));
	}

	public void moveLocalXZ(float leftRight, float forwardsBackwards) {
		// Calculate the movement vector in the camera's local coordinate system
		position.add(rotation.positiveZ(new Vector3f()).mul(forwardsBackwards));
		position.add(rotation.positiveX(new Vector3f()).mul(leftRight));
	}
	
	public Matrix4f updateMatrix() {
		viewMatrix.identity();
		viewMatrix.rotate(rotation);
		viewMatrix.translate(-position.x, -position.y, -position.z);
		return viewMatrix;
	}
	
}
