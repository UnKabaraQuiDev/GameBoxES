package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.QuadMesh;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.SubEntitiesComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.text.TextEmitter;
import lu.kbra.gamebox.client.es.engine.utils.consts.Alignment;
import lu.kbra.gamebox.client.es.game.game.utils.GlobalUtils;

public class UITextButton extends Entity {

	private MeshComponent meshComponent;
	private CacheManager cache;
	private Transform3DComponent transformComponent;
	private UITextLabel textLabelEntity;

	public UITextButton(CacheManager cache, String name, String txt, Vector3f pos, Alignment alignment, String btnType) {
		this.cache = cache;
		
		textLabelEntity = new UITextLabel(cache, name, txt, pos, alignment);
		addComponent(new SubEntitiesComponent(textLabelEntity));

		meshComponent = GlobalUtils.createUIButton(cache, name + "-btn", btnType);
		addComponent(meshComponent);

		addComponent(transformComponent = new Transform3DComponent(pos));
		
		adjustScale();
	}

	public void adjustScale() {
		TextEmitter emit = textLabelEntity.getTextEmitterComponent().getTextEmitter(cache);
		transformComponent.getTransform().setScale(emit.computeMaxWidthCount()*((QuadMesh) emit.getMesh()).getSize().x());
	}
	
	public void setPosition(Vector3f pos) {
		TextEmitter emit = textLabelEntity.getTextEmitterComponent().getTextEmitter(cache);
		
		textLabelEntity.getTransformComponent().getTransform().setTranslation(pos.add(0, -((QuadMesh) emit.getMesh()).getSize().y()/2, 0.01f, new Vector3f())).updateMatrix();
		transformComponent.getTransform().setTranslation(pos).updateMatrix();
	}

}
