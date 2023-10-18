package lu.pcy113.pdr.engine.graph.composition;

import java.util.logging.Level;

import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.IntAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.utils.Logger;

public class PassRenderLayer extends RenderLayer<GameEngine, Mesh> {
	
	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";
	
	private static Mesh SCREEN = new Mesh(
			"PASS_SCREEN", null,
			new FloatAttribArray("pos", 0, 3, new float[] {
					-1, 1, 0,
					1, 1, 0,
					1, -1, 0,
					-1, -1, 0
			}),
			new IntAttribArray("ind", -1, 1, new int[] {
					0, 1, 2,
					0, 2, 3
			}),
			new FloatAttribArray("uv", 1, 2, new float[] {
					0, 1,
					1, 1,
					1, 0,
					0, 0
			})
			);
	
	protected String material;
	
	public PassRenderLayer(String name, String material) {
		super(name, SCREEN);
		this.material = material;
	}
	
	@Override
	public void render(CacheManager cache, GameEngine engine) {
		Logger.log(Level.INFO, "PassRenderLayer : m:"+material);
		
		/*MeshRenderer renderer = (MeshRenderer) cache.getRenderer(Mesh.NAME);
		if(renderer == null) {
			Logger.log(Level.SEVERE, "No renderer found for: "+Mesh.NAME);
			return;
		}
		
		renderer.render(cache, null, (Mesh) target);*/
		
		target.bind();
		
		Material material = cache.getMaterial(this.material);
		if(material == null)
			return;
		Shader shader = cache.getShader(material.getShader());
		if(shader == null)
			return;
		
		shader.bind();
		
		material.bindProperties(cache, this, shader);
		
		if(shader.hasUniform(SCREEN_WIDTH)) {
			shader.setUniform(SCREEN_WIDTH, engine.getWindow().getWidth());
		}
		if(shader.hasUniform(SCREEN_HEIGHT)) {
			shader.setUniform(SCREEN_HEIGHT, engine.getWindow().getHeight());
		}
		
		//GL40.glDisable(GL40.GL_DEPTH_TEST);
		//GL40.glDepthMask(false);
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, target.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);
		
		//GL40.glDepthMask(true);
		
		target.unbind();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		
		if(SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}
	
}
