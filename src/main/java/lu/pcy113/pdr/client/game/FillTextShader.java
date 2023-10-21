package lu.pcy113.pdr.client.game;

import lu.pcy113.pdr.engine.graph.material.text.TextShader;

public class FillTextShader extends TextShader {
	
	public static final String NAME = FillTextShader.class.getName();
	
	public static final String FILL_COLOR = "fillColor";
	
	public FillTextShader() {
		super("fill", true);
	}
	
	@Override
	public void createUniforms() {
		super.createUniforms();
		
		// frag
		createUniform(FILL_COLOR);
	}
	
}
