package lu.pcy113.pdr.engine.utils;

import org.joml.Vector3f;

public class Ray {
	
	private Vector3f origin, dir;
	
	public Ray(Vector3f origin, Vector3f dir) {
		this.origin = origin;
		this.dir = dir;
	}
	
	public Vector3f getOrigin() {
		return origin;
	}
	public void setOrigin(Vector3f origin) {
		this.origin = origin;
	}
	public Vector3f getDir() {
		return dir;
	}
	public void setDir(Vector3f dir) {
		this.dir = dir;
	}
	public Vector3f getEndPoint(float length) {
		return origin.add(dir.mul(length, new Vector3f()), new Vector3f());
	}
	
}
