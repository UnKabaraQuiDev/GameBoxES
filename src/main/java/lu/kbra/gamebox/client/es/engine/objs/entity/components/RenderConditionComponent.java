package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import java.util.function.Supplier;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public class RenderConditionComponent extends Component {

	private Supplier<Boolean> supplier;

	public RenderConditionComponent(Supplier<Boolean> sup) {
		this.supplier = sup;
	}

	public boolean get() {
		return supplier.get();
	}

}
