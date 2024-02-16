package lu.pcy113.pdr.engine.graph.composition;

import java.util.Map.Entry;
import java.util.logging.Level;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec2fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.graph.texture.Texture;

public class PassRenderLayer extends RenderLayer<GameEngine, Framebuffer, Mesh> {

	public static final String SCREEN_WIDTH = "screen_width";
	public static final String SCREEN_HEIGHT = "screen_height";

	private static Mesh SCREEN = new Mesh(
			"PASS_SCREEN",
			null,
			new Vec3fAttribArray("pos", 0, 1, new Vector3f[] { new Vector3f(-1, 1, 0), new Vector3f(1, 1, 0), new Vector3f(1, -1, 0), new Vector3f(-1, -1, 0) }),
			new UIntAttribArray("ind", -1, 1, new int[] { 0, 1, 2, 0, 2, 3 }, GL40.GL_ELEMENT_ARRAY_BUFFER),
			new Vec2fAttribArray("uv", 1, 1, new Vector2f[] { new Vector2f(0, 1), new Vector2f(1, 1), new Vector2f(1, 0), new Vector2f(0, 0) }));

	protected Material material;

	public PassRenderLayer(String name, Material material) {
		super(name, SCREEN);
		this.material = material;
	}
	
	@Override
	public void render(CacheManager cache, GameEngine engine, Framebuffer fb) {
		GlobalLogger.log(Level.INFO, "PassRenderLayer : m:" + material);

		target.bind();

		Material material = this.material;
		if (material == null) {
			GlobalLogger.log(Level.WARNING, "Material is null!");
			return;
		}
		RenderShader shader = material.getRenderShader();
		if (shader == null) {
			GlobalLogger.log(Level.WARNING, "Shader is null!");
			return;
		}

		shader.bind();
		
		material.setPropertyIfPresent(SCREEN_HEIGHT, engine.getWindow().getHeight());
		material.setPropertyIfPresent(SCREEN_WIDTH, engine.getWindow().getWidth());
		
		for(Entry<Integer, Texture> attachments : fb.getAttachments().entrySet()) {
			// GlobalLogger.info("Attachment: "+shader.getUniform(attachments.getValue().getId())+" "+material.getProperty(attachments.getValue().getId())+" "+attachments);
			// material.setProperty(attachments.getValue().getId(), attachments.getValue().getTid());
			
			int id = shader.getUniformLocation(attachments.getValue().getId());
			if(id != -1) {
				attachments.getValue().bind(id);
			}
		}
		
		material.bindProperties(cache, this, shader);
		
		if (shader.isTransparent()) {
			GL40.glEnable(GL40.GL_BLEND);
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		GL40.glDisable(GL40.GL_DEPTH_TEST);
		
		GL40.glDrawElements(GL40.GL_TRIANGLES, target.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);
		
		GL40.glDisable(GL40.GL_BLEND);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
		
		target.unbind();
	}

	@Override
	public void cleanup() {
		super.cleanup();

		if (SCREEN != null) {
			SCREEN.cleanup();
			SCREEN = null;
		}
	}

}
