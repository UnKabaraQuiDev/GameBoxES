package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UITextLabel extends Entity {

	private TextEmitterComponent textEmitterComponent;
	private Transform3DComponent transformComponent;

	public UITextLabel(CacheManager cache, String name, String txt, Vector3f pos, Alignment alignment) {
		textEmitterComponent = GlobalUtils.createUIText(cache, name, txt, alignment);
		addComponent(textEmitterComponent);

		addComponent(transformComponent = new Transform3DComponent(pos));
	}

	public TextEmitterComponent getTextEmitterComponent() {
		return textEmitterComponent;
	}

	public Transform3DComponent getTransformComponent() {
		return transformComponent;
	}

}
