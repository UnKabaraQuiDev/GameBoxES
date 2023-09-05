package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;

public class TxtDiffuse1Shader extends Shader {
	
	public static final String NAME = "diffuse-txt1";
	
	public static final String VIEW_POS = "viewPos";
	public static final String SHININESS = "shininess";
	public static final String LIGHT_COUNT = "lightCount";
	public static final String LIGHTS = "lights";
	public static final String AMBIENT_COLOR = "ambient";
	
	public TxtDiffuse1Shader() {
		super(NAME,
				new ShaderPart("./resources/shaders/diffuse/diffuse.vert"),
				new ShaderPart("./resources/shaders/diffuse/txtDiffuse1.frag"));
	}
	
	@Override
	public void createUniforms() {
		// vert
		getUniform(Shader.PROJECTION_MATRIX);
		getUniform(Shader.VIEW_MATRIX);
		getUniform(Shader.TRANSFORMATION_MATRIX);
		
		// frag
		getUniform(VIEW_POS);
		getUniform(SHININESS);
		getUniform(LIGHT_COUNT);
		getUniform(LIGHTS);
		getUniform(AMBIENT_COLOR);
	}

}
