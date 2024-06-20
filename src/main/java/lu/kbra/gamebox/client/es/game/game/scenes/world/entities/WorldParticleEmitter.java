package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.FloatAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.geom.instance.InstanceEmitter;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform;
import lu.kbra.gamebox.client.es.game.game.render.shaders.WorldParticleShader.WorldParticleMaterial;

public class WorldParticleEmitter extends InstanceEmitter {

	public WorldParticleEmitter(String name, int count, WorldParticleMaterial material, Transform baseTransform) {
		super(name, Mesh.newQuad(name + name.hashCode(), material, new Vector2f(1)), count, baseTransform, new AttribArray[] { new FloatAttribArray("size", 7, 1, new float[count], false, 1) });
	}

}
