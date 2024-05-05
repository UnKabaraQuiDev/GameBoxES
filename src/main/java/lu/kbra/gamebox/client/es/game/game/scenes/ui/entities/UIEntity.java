package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;

public abstract class UIEntity extends Entity {

	protected boolean selected = false;

	public UIEntity(String str) {
		super(str);
	}
	
	public abstract void updateUI();

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return selected;
	}

}
