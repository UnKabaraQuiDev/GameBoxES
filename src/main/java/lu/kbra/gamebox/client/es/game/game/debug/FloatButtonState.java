package lu.kbra.gamebox.client.es.game.game.debug;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.debug.shaders.FloatButtonStateShader;
import lu.kbra.gamebox.client.es.game.game.debug.shaders.FloatButtonStateShader.FloatButtonStateMaterial;

public class FloatButtonState extends Entity {

	private Mesh mesh;
	private FloatButtonStateMaterial material;

	public FloatButtonState(CacheManager cache, Transform3D transform3d) {
		this(cache, transform3d, GeoPlane.XY);
	}
	
	public FloatButtonState(CacheManager cache, Transform3D transform3d, GeoPlane plane) {
		if (cache.hasRenderShader(FloatButtonStateShader.NAME)) {
			FloatButtonStateShader shader = (FloatButtonStateShader) cache.getRenderShader(FloatButtonStateShader.NAME);
			this.material = (FloatButtonStateMaterial) new FloatButtonStateMaterial(shader);
			cache.addMaterial(material);
		} else
			this.material = (FloatButtonStateMaterial) cache.loadMaterial(FloatButtonStateMaterial.class);
		this.mesh = Mesh.newQuad(plane, "float_button_state-" + hashCode(), material, new Vector2f(0.2f, 1));
		cache.addMesh(mesh);

		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(transform3d));

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
