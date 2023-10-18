package lu.pcy113.pdr.client.game;

import java.util.HashMap;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.TextureMaterial;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;

public class TxtDiffuseMaterial extends TextureMaterial {

	@SuppressWarnings("serial")
	public TxtDiffuseMaterial(String name,
			String diffTexture, Vector3f ambient,
			String specTexture, float shininess) {
		super(name,
				//TxtDiffuse1Shader.LIGHTS, TxtDiffuse1Shader.LIGHT_COUNT,
				TxtDiffuse1Shader.NAME,
				new HashMap<String, String>() {{
					put("txtDiffuse0", diffTexture);
					put("txtSpecular1", specTexture);
				}});
		
		super.setProperty(TxtDiffuse1Shader.SHININESS, shininess);
		super.setProperty(TxtDiffuse1Shader.AMBIENT_COLOR, ambient);
	}
	
	@Override
	public void bindProperties(CacheManager cache, Renderable scene, Shader shader) {
		if(scene instanceof Scene)
		super.setProperty(DiffuseShader.VIEW_POS, ((Camera3D) ((Scene) scene).getCamera()).getPosition());
		
		super.bindProperties(cache, scene, shader);
	}
	
}
