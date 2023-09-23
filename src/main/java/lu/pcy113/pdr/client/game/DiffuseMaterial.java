package lu.pcy113.pdr.client.game;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.scene.Camera3D;
import lu.pcy113.pdr.engine.scene.Scene;

public class DiffuseMaterial extends Material {
	
	public DiffuseMaterial(String name,
			Vector3f diffuse, Vector3f specular, Vector3f ambient,
			float shininess) {
		super(name,
				//DiffuseShader.LIGHTS, DiffuseShader.LIGHT_COUNT,
				DiffuseShader.NAME);
		
		super.setProperty(DiffuseShader.DIFFUSE_COLOR, diffuse);
		super.setProperty(DiffuseShader.SPECULAR_COLOR, diffuse);
		super.setProperty(DiffuseShader.SHININESS, shininess);
		super.setProperty(DiffuseShader.AMBIENT_COLOR, ambient);
	}
	
	@Override
	public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
		if(scene instanceof Scene)
			super.setProperty(DiffuseShader.VIEW_POS, ((Camera3D) ((Scene) scene).getCamera()).getPosition());
		
		super.bindProperties(cache, scene, shader);
	}
	
}
