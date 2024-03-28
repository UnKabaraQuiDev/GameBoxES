package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform;

public abstract class TransformComponent extends Component {

	public abstract Transform getTransform();

}
