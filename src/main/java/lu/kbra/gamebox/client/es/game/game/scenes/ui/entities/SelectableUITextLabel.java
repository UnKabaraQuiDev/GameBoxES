package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.text.TextShader.TextMaterial;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalConsts;

public class SelectableUITextLabel extends UITextLabel {

	public SelectableUITextLabel(CacheManager cache, String name, String txt, Vector3f pos, Alignment alignment, UIInteractRunnable run) {
		super(cache, name, txt, pos, alignment, run);
	}

	public SelectableUITextLabel(CacheManager cache, String name, String txt, Vector3f pos, Alignment alignment) {
		super(cache, name, txt, pos, alignment);
	}

	@Override
	public void updateUI() {
		if (selected) {
			textEmitterComponent.getTextEmitter(cache).setText(">" + text + "<").updateText();
			((TextMaterial) textEmitterComponent.getTextEmitter(cache).getInstances().getParticleMesh().getMaterial()).setFgColor(GlobalConsts.HIGHLIGHT);
		} else {
			textEmitterComponent.getTextEmitter(cache).setText(text).updateText();
			((TextMaterial) textEmitterComponent.getTextEmitter(cache).getInstances().getParticleMesh().getMaterial()).setFgColor(GlobalConsts.NEUTRAL);
		}
	}

}
