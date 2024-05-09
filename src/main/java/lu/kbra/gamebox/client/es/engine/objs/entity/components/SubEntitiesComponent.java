package lu.kbra.gamebox.client.es.engine.objs.entity.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;

public class SubEntitiesComponent extends Component {

	private List<Entity> entities = new ArrayList<>();

	public SubEntitiesComponent(Entity... entity) {
		Arrays.stream(entity).forEach(entities::add);
	}
	
	public SubEntitiesComponent(Entity entity) {
		entities.add(entity);
	}
	
	public List<Entity> getEntities() {
		return entities;
	}

}
