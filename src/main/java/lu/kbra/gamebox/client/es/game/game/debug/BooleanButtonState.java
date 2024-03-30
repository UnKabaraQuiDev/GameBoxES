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
import lu.kbra.gamebox.client.es.game.game.debug.shaders.BooleanButtonStateShader;
import lu.kbra.gamebox.client.es.game.game.debug.shaders.BooleanButtonStateShader.BooleanButtonStateMaterial;

public class BooleanButtonState extends Entity {

	private Mesh mesh;
	private BooleanButtonStateMaterial material;

	public BooleanButtonState(CacheManager cache, Transform3D transform3d) {
		this(cache, transform3d, GeoPlane.XY);
	}

	public BooleanButtonState(CacheManager cache, Transform3D transform3d, GeoPlane plane) {
		if (cache.hasRenderShader(BooleanButtonStateShader.NAME)) {
			BooleanButtonStateShader shader = (BooleanButtonStateShader) cache.getRenderShader(BooleanButtonStateShader.NAME);
			this.material = (BooleanButtonStateMaterial) new BooleanButtonStateMaterial(shader);
			cache.addMaterial(material);
		} else
			this.material = (BooleanButtonStateMaterial) cache.loadMaterial(BooleanButtonStateMaterial.class);
		this.mesh = Mesh.newQuad(plane, "boolean_button_state-" + hashCode(), material, new Vector2f(0.5f, 0.2f));
		cache.addMesh(mesh);

		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(transform3d));

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
