package lu.pcy113.pdr.client.game.three;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pdr.client.game.three.FourButtonStateShader.FourButtonStateMaterial;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;

public class FourButtonState extends Entity {
	
	private Mesh mesh;
	private FourButtonStateMaterial material;
	
	public FourButtonState(CacheManager cache, Vector3f pos) {
		if(cache.hasRenderShader(FourButtonStateShader.NAME)) {
			FourButtonStateShader shader = (FourButtonStateShader) cache.getRenderShader(FourButtonStateShader.NAME);
			this.material = (FourButtonStateMaterial) new FourButtonStateMaterial(shader);
			cache.addMaterial(material);
		}else
			this.material = (FourButtonStateMaterial) cache.loadMaterial(FourButtonStateMaterial.class);
		this.mesh = Mesh.newQuad("float_button_state-"+hashCode(), material, new Vector2f(1));
		cache.addMesh(mesh);
		
		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(pos));
		
		material.setButtons(new Vector4f(0));
	}
	
	public void setButtons(Vector4f buttons) {
		material.setButtons(buttons);
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
}
