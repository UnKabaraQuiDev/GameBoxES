package lu.kbra.gamebox.client.es.engine.graph.composition;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.pcy113.pclib.logger.GlobalLogger;

public abstract class RenderLayer<K, J, O extends Renderable> implements Renderable, UniqueID, Cleanupable {

	protected final String name;
	protected boolean visible = true;
	protected O target;

	public RenderLayer(String name, O t) {
		this.target = t;
		this.name = name;
	}

	public abstract void render(K container, J parent);

	public O getTarget() {
		return target;
	}

	public void setTarget(O target) {
		this.target = target;
	}

	public boolean isVisible() {
		return visible;
	}

	public RenderLayer<K, J, O> setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: "+name);
	}

	@Override
	public String getId() {
		return name;
	}

}
