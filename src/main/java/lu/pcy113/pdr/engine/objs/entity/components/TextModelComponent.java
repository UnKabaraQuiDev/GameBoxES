package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.text.TextModel;

public class TextModelComponent extends Component implements Renderable {

	private String textModelId;
	private int textSize = 64;

	public TextModelComponent(TextModel textTextModel) {
		this.textModelId = textTextModel.getId();
	}

	public TextModelComponent(String textTextModelId) {
		this.textModelId = textTextModelId;
	}

	public String getTextModelId() {
		return this.textModelId;
	}

	public void setTextModelId(String textTextModelId) {
		this.textModelId = textTextModelId;
	}

	public TextModel getTextModel(CacheManager cache) {
		return cache.getTextModel(this.textModelId);
	}

	public void setTextModel(TextModel textTextModel) {
		this.textModelId = textTextModel.getId();
	}

	public int getTextSize() {
		return this.textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

}
