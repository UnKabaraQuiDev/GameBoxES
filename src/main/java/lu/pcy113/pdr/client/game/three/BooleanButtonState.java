package lu.pcy113.pdr.client.game.three;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pdr.client.game.three.BooleanButtonStateShader.BooleanButtonStateMaterial;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;

public class BooleanButtonState extends Entity {
	
	private Mesh mesh;
	private BooleanButtonStateMaterial material;
	
	public BooleanButtonState(CacheManager cache, Vector3f pos) {
		if(cache.hasRenderShader(BooleanButtonStateShader.NAME)) {
			BooleanButtonStateShader shader = (BooleanButtonStateShader) cache.getRenderShader(BooleanButtonStateShader.NAME);
			this.material = (BooleanButtonStateMaterial) new BooleanButtonStateMaterial(shader);
			cache.addMaterial(material);
		}else
			this.material = (BooleanButtonStateMaterial) cache.loadMaterial(BooleanButtonStateMaterial.class);
		this.mesh = Mesh.newQuad("float_button_state-"+hashCode(), material, new Vector2f(0.5f, 0.2f));
		cache.addMesh(mesh);
		
		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(pos));
		
		material.setValue(0);
	}
	
	public void setValue(float value) {
		material.setValue(value);
	}
	public void setColor(Vector4f color) {
		material.setColor(color);
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
}
