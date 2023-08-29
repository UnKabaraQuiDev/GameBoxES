package lu.pcy113.pdr.engine.graph.renderer;

import org.lwjgl.opengl.GL11;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.shader.TextureCache;
import lu.pcy113.pdr.engine.impl.ChildObject;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.utils.Logger;

public abstract class Renderer<T> implements Cleanupable, ChildObject<Scene> {
	
	protected TextureCache textures;
	protected Scene scene;
	
	public Renderer() {
		Logger.log();
		
		textures = new TextureCache();
	}
	
	public void render(Window window, T obj) {
		if(obj == null)
			throw new RuntimeException("No Object instance passed as render information.");
		if(scene == null)
			throw new RuntimeException("No Scene instance attached to Renderer.");
		
		Logger.log();
		
		GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
	}
	
	@Override
	public void cleanup() {
		textures.cleanup();
	}
	
	@Override
	public void attach(Scene obj) {this.scene = obj;}
	@Override
	public Scene getAttached() {return this.scene;}
	
	public TextureCache getTextures() {return textures;}
	public void setTextures(TextureCache textures) {this.textures = textures;}

}
