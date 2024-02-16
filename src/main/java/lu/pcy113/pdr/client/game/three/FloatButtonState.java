package lu.pcy113.pdr.client.game.three;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pdr.client.game.three.FloatButtonStateShader.FloatButtonStateMaterial;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;

public class FloatButtonState extends Entity {
	
	private Mesh mesh;
	private FloatButtonStateMaterial material;
	
	public FloatButtonState(CacheManager cache, Vector3f pos) {
		if(cache.hasRenderShader(FloatButtonStateShader.NAME)) {
			FloatButtonStateShader shader = (FloatButtonStateShader) cache.getRenderShader(FloatButtonStateShader.NAME);
			this.material = (FloatButtonStateMaterial) new FloatButtonStateMaterial(shader);
			cache.addMaterial(material);
		}else
			this.material = (FloatButtonStateMaterial) cache.loadMaterial(FloatButtonStateMaterial.class);
		this.mesh = Mesh.newQuad("float_button_state-"+hashCode(), material, new Vector2f(0.2f, 1));
		cache.addMesh(mesh);
		
		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(pos));
		
		material.setColor(new Vector4f(0, 1, 0, 1));
		material.setValue(0);
		material.setRadius(0.02f);
		material.setThreshold(0.05f);
	}
	
	public void setRadius(float radius) {
		material.setRadius(radius);
	}
	public void setColor(Vector4f color) {
		material.setColor(color);
	}
	public void setValue(float value) {
		material.setValue(value);
	}
	public void setThreshold(float threshold) {
		material.setThreshold(threshold);
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
}
