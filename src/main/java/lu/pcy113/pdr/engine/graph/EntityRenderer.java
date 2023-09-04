package lu.pcy113.pdr.engine.graph;

import lu.pcy113.pdr.engine.geom.Entity;

public class EntityRenderer extends Renderer<Entity> {

	public EntityRenderer(RenderManager rm) {
		super(rm, Entity.class);
	}

	@Override
	public void prepare() {
		
	}

	@Override
	public void render(Entity obj) {
		obj.bind();
		
		obj.unbind();
	}
	
}
