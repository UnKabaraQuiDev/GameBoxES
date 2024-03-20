package lu.pcy113.pdr.engine.objs.entity.components;

import org.joml.Vector2f;

import lu.pcy113.pdr.engine.objs.entity.Component;

public abstract class UIComponent extends Component {
	
	public abstract boolean contains(Vector2f point);
	
}
