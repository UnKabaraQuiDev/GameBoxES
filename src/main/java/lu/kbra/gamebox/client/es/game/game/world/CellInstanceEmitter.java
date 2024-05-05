package lu.kbra.gamebox.client.es.game.game.world;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.IntAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.InstanceEmitter;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellInstanceShader.CellInstanceMaterial;

public class CellInstanceEmitter extends InstanceEmitter {

	public CellInstanceEmitter(String name, int count, CellInstanceMaterial material, Transform baseTransform) {
		super(name, Mesh.newQuad(name + name.hashCode(), material, new Vector2f(1)), count, baseTransform, new AttribArray[] { new IntAttribArray("state", 7, 1, new int[count], false, 1) });

		createDrawBuffer();
	}

	public void createDrawBuffer() {
		Mesh quad = super.getParticleMesh();

		quad.createDrawBuffer();
		quad.getDrawBuffer().bind();
		quad.getDrawBuffer().setInstancesCount(super.getParticleCount());
		quad.getDrawBuffer().unbind();
	}
	
}
