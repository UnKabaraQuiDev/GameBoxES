package lu.pcy113.pdr.engine.objs;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.impl.UniqueID;

public class PointLight implements UniqueID {

	public static final String POSITION = "position";
	public static final String CONSTANT = "constant";
	public static final String LINEAR = "linear";
	public static final String QUADRATIC = "quadratic";
	public static final String AMBIENT_COLOR = "ambientColor";
	public static final String DIFFUSE_COLOR = "diffuseColor";
	public static final String SPECULAR_COLOR = "specularColor";

	private final String name;

	private float constant, linear, quadratic;
	private Vector3f ambient, diffuse, specular;

	public PointLight(String name, Vector3f position, float constant, float linear, float quadratic, Vector3f ambient,
			Vector3f diffuse, Vector3f specular) {
		this.name = name;
		this.constant = constant;
		this.linear = linear;
		this.quadratic = quadratic;
		this.ambient = ambient;
		this.diffuse = diffuse;
		this.specular = specular;
	}

	public void bind(Material mat, String arr, int i) {
		String ind = arr + "[" + i + "].";

		// mat.setProperty(ind+POSITION, position);

		mat.setProperty(ind + CONSTANT, constant);
		mat.setProperty(ind + LINEAR, linear);
		mat.setProperty(ind + QUADRATIC, quadratic);

		mat.setProperty(ind + AMBIENT_COLOR, ambient);
		mat.setProperty(ind + DIFFUSE_COLOR, diffuse);
		mat.setProperty(ind + SPECULAR_COLOR, specular);
	}

	@Override
	public String getId() {
		return name;
	}

	public float getConstant() {
		return constant;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getLinear() {
		return linear;
	}

	public void setLinear(float linear) {
		this.linear = linear;
	}

	public float getQuadratic() {
		return quadratic;
	}

	public void setQuadratic(float quadratic) {
		this.quadratic = quadratic;
	}

	public Vector3f getAmbient() {
		return ambient;
	}

	public void setAmbient(Vector3f ambient) {
		this.ambient = ambient;
	}

	public Vector3f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vector3f diffuse) {
		this.diffuse = diffuse;
	}

	public Vector3f getSpecular() {
		return specular;
	}

	public void setSpecular(Vector3f specular) {
		this.specular = specular;
	}

}
