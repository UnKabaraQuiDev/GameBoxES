package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public abstract class UIComponent extends Component {

	public abstract boolean contains(Vector2f point);

	public abstract void hover(Vector2f pos);

	public abstract void  click(Vector2f pos);

}
