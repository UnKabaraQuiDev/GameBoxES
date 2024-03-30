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
import lu.kbra.gamebox.client.es.game.game.debug.shaders.FourButtonStateShader;
import lu.kbra.gamebox.client.es.game.game.debug.shaders.FourButtonStateShader.FourButtonStateMaterial;

public class FourButtonState extends Entity {

	private Mesh mesh;
	private FourButtonStateMaterial material;

	public FourButtonState(CacheManager cache, Transform3D transform3d) {
		this(cache, transform3d, GeoPlane.XY);
	}
	
	public FourButtonState(CacheManager cache, Transform3D transform3d, GeoPlane plane) {
		if (cache.hasRenderShader(FourButtonStateShader.NAME)) {
			FourButtonStateShader shader = (FourButtonStateShader) cache.getRenderShader(FourButtonStateShader.NAME);
			this.material = (FourButtonStateMaterial) new FourButtonStateMaterial(shader);
			cache.addMaterial(material);
		} else
			this.material = (FourButtonStateMaterial) cache.loadMaterial(FourButtonStateMaterial.class);
		this.mesh = Mesh.newQuad(plane, "four_button_state-" + hashCode(), material, new Vector2f(1));
		cache.addMesh(mesh);

		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(transform3d));

		material.setButtons(new Vector4f(0));
	}

	public void setButtons(Vector4f buttons) {
		material.setButtons(buttons);
	}

	public Mesh getMesh() {
		return mesh;
	}

}
