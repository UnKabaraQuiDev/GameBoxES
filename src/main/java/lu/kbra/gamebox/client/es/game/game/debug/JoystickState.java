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
import lu.kbra.gamebox.client.es.game.game.debug.shaders.JoystickStateShader;
import lu.kbra.gamebox.client.es.game.game.debug.shaders.JoystickStateShader.JoystickStateMaterial;

public class JoystickState extends Entity {

	private Mesh mesh;
	private JoystickStateMaterial material;

	public JoystickState(CacheManager cache, Transform3D transform) {
		this(cache, transform, GeoPlane.XY);
	}
	
	public JoystickState(CacheManager cache, Transform3D transform, GeoPlane plane) {
		if (cache.hasRenderShader(JoystickStateShader.NAME)) {
			JoystickStateShader shader = (JoystickStateShader) cache.getRenderShader(JoystickStateShader.NAME);
			this.material = (JoystickStateMaterial) new JoystickStateMaterial(shader);
			cache.addMaterial(material);
		} else
			this.material = (JoystickStateMaterial) cache.loadMaterial(JoystickStateMaterial.class);
		this.mesh = Mesh.newQuad(plane, "joystick_state-" + hashCode(), material, new Vector2f(1));
		cache.addMesh(mesh);

		super.addComponent(new MeshComponent(mesh));
		super.addComponent(new Transform3DComponent(transform));

		material.setColor(new Vector4f(0, 1, 0, 1));
		material.setPosition(new Vector2f(0, 0));
		material.setRadius(0.05f);
		material.setThreshold(0.05f);
	}

	public void setRadius(float radius) {
		material.setRadius(radius);
	}

	public void setColor(Vector4f color) {
		material.setColor(color);
	}

	public void setPosition(Vector2f position) {
		if (position.length() > 1)
			position.normalize();
		material.setPosition(position);
	}

	public void setThreshold(float threshold) {
		material.setThreshold(threshold);
	}

	public void setButton(float btn) {
		material.setButton(btn);
	}

	public Mesh getMesh() {
		return mesh;
	}

}
