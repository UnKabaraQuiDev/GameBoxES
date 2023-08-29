package lu.pcy113.pdr.engine.graph.shader;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.graph.UniformsMap;
import lu.pcy113.pdr.engine.impl.Cleanupable;

public class Material implements Cleanupable {
	
	private ShaderProgram shader;
	
	private final boolean texture;
	private final Texture texturePtr;
	
	public Material(Texture texture, ShaderProgram shader) {
		this.texture = texture != null;
			
		this.texturePtr = texture;
		this.shader = shader;
		
		createUniforms(shader.getUniformsMap());
	}
	
	protected void createUniforms(UniformsMap uniforms) {
		if(texture) {
			uniforms.createUniform("txtSampler");
		}
	}
	protected void bindUniforms(UniformsMap uniforms) {}

	public UniformsMap bind() {
		UniformsMap map = shader.bind();
		bindUniforms(map);
		
		if(texture) {
			map.setUniform("txtSampler", 0);
			GL30.glActiveTexture(GL30.GL_TEXTURE0);
			texturePtr.bind();
		}
		
		return map;
	}
	public void unbind() {
		shader.unbind();
	}
	
	@Override
	public void cleanup() {
		shader.cleanup();
		if(texturePtr != null)
			texturePtr.cleanup();
	}
	
	public ShaderProgram getShader() {return shader;}
	public Texture getTexture() {return texturePtr;}
	public boolean hasTexture() {return texture;}

}
