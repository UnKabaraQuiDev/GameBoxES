package lu.pcy113.pdr.engine.graph;

import lu.pcy113.pdr.engine.geom.Entity;
import lu.pcy113.pdr.engine.scene.Scene3D;

public class Scene3DRenderer extends Renderer<Scene3D> {
	
	public Scene3DRenderer(RenderManager rm) {
		super(rm, Scene3D.class);
	}

	@Override
	public void prepare() {
		
	}
	
	@Override
	public void render(Scene3D obj) {
		EntityRenderer eR = (EntityRenderer) getRenderManager().getRenderer(Entity.class);
		for(Entity e : obj.getEntities().values()) {
			eR.render(e);
		}
	}
	
}
