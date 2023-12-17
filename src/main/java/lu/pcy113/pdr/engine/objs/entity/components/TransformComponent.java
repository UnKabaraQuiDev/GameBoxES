package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public abstract class TransformComponent extends Component {

	public abstract Transform getTransform();

}
