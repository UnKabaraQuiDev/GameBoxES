package lu.pcy113.pdr.engine.scene;

import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.renderer.Renderer;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueId;
import lu.pcy113.pdr.utils.Logger;

public class Scene implements Cleanupable, UniqueId {
	
	private final String id;
	
	private Renderer<Scene> renderer;
	
	private boolean visible = true;
	private Projection projection;
	
	public Scene(String id, int w, int h, Renderer<Scene> renderer) {
		Logger.log();
		
		this.id = id;
		
		this.projection = new Projection(w, h);
		this.renderer = renderer;
		
		renderer.attach(this);
	}
	
	public void render(Window window) {
		renderer.render(window, this);
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		renderer.cleanup();
	}
	
	@Override
	public String getId() {return id;}
	public Projection getProjection() {return projection;}
	public void setProjection(Projection projection) {this.projection = projection;}
	public Renderer getRenderer() {return renderer;}
	public boolean isVisible() {return visible;}
	public void setVisible(boolean visible) {this.visible = visible;}
	
	public void resize(int width, int height) {
		projection.persp_UpdateProjMatrix(width, height);
	}
	
}
