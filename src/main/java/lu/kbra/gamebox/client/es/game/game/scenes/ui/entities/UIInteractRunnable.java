package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;

@FunctionalInterface
public interface UIInteractRunnable {

	void interact(Entity entity, boolean direction, Direction dir, Button button);
	
}
