package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;

public class CellsEntity extends Entity {
	
	private Transform3DComponent transform;

	public CellsEntity(String str, Component... cs) {
		super(str, cs);

		transform = getComponent(Transform3DComponent.class);
	}

	public Transform3DComponent getTransform() {
		return transform;
	}

}
