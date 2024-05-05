package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TextEmitterComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.engine.utils.consts.Button;
import lu.kbra.gamebox.client.es.engine.utils.consts.Direction;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class UITextLabel extends UIEntity {

	protected CacheManager cache;
	protected String text;

	protected TextEmitterComponent textEmitterComponent;
	protected Transform3DComponent transformComponent;

	protected UIInteractRunnable run;

	public UITextLabel(CacheManager cache, String name, String txt, Vector3f pos, Alignment alignment) {
		super(name);
		
		this.cache = cache;
		this.text = txt;

		textEmitterComponent = GlobalUtils.createUIText(cache, name, txt.length() + 12, txt, alignment);
		addComponent(textEmitterComponent);

		addComponent(transformComponent = new Transform3DComponent(pos));
	}

	public UITextLabel(CacheManager cache, String name, String txt, Vector3f pos, Alignment alignment, UIInteractRunnable run) {
		this(cache, name, txt, pos, alignment);
		
		this.run = run;
	}

	@Override
	public void updateUI() {
		// super.getComponent(TextEmitterComponent.class).getTextEmitter(cache).setText(text).updateText();
	}

	public void interact(boolean direction, Direction dir, Button button) {
		if (run != null) {
			run.interact(this, direction, dir, button);
		}
	}

	public TextEmitterComponent getTextEmitterComponent() {
		return textEmitterComponent;
	}

	public Transform3DComponent getTransformComponent() {
		return transformComponent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
