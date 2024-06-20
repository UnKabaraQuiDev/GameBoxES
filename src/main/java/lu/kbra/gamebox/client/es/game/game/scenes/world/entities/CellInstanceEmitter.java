package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import java.util.Collections;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.IntAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.InstanceEmitter;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform;
import lu.kbra.gamebox.client.es.game.game.data.CellDescriptor;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellInstanceShader.CellInstanceMaterial;

public class CellInstanceEmitter extends InstanceEmitter {

	private List<Vector3f> directions;
	private CellDescriptor description;

	public CellInstanceEmitter(String name, int count, CellInstanceMaterial material, Transform baseTransform, CellDescriptor desc) {
		super(name, Mesh.newQuad(name + name.hashCode(), material, new Vector2f(1)), count, baseTransform, new AttribArray[] { new IntAttribArray("state", 7, 1, new int[count], false, 1) });

		directions = Collections.nCopies(count, new Vector3f());
		this.description = desc;
	}

	public List<Vector3f> getDirections() {
		return directions;
	}

	public CellDescriptor getDescription() {
		return description;
	}

}
