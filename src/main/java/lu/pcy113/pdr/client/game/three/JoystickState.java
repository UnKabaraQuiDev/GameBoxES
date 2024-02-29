package lu.pcy113.pdr.client.game.three;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.pcy113.pdr.client.game.three.JoystickStateShader.JoystickStateMaterial;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.Transform3DComponent;

public class JoystickState extends Entity {

	private Mesh mesh;
	private JoystickStateMaterial material;

	public JoystickState(CacheManager cache, Vector3f pos) {
		if (cache.hasRenderShader(
				JoystickStateShader.NAME)) {
			JoystickStateShader shader = (JoystickStateShader) cache.getRenderShader(
					JoystickStateShader.NAME);
			this.material = (JoystickStateMaterial) new JoystickStateMaterial(
					shader);
			cache.addMaterial(
					material);
		} else
			this.material = (JoystickStateMaterial) cache.loadMaterial(
					JoystickStateMaterial.class);
		this.mesh = Mesh.newQuad(
				"joystick_state-" + hashCode(),
				material,
				new Vector2f(
						1));
		cache.addMesh(
				mesh);
		// cache.loadMesh("joystick_state-"+hashCode(), material, "./resources/models/plane.obj");

		super.addComponent(
				new MeshComponent(
						mesh));
		super.addComponent(
				new Transform3DComponent(
						pos));

		material.setColor(
				new Vector4f(
						0,
						1,
						0,
						1));
		material.setPosition(
				new Vector2f(
						0,
						0));
		material.setRadius(
				0.05f);
		material.setThreshold(
				0.05f);
	}

	public void setRadius(float radius) {
		material.setRadius(
				radius);
	}

	public void setColor(Vector4f color) {
		material.setColor(
				color);
	}

	public void setPosition(Vector2f position) {
		if (position.length() > 1)
			position.normalize();
		material.setPosition(
				position);
	}

	public void setThreshold(float threshold) {
		material.setThreshold(
				threshold);
	}

	public void setButton(float btn) {
		material.setButton(
				btn);
	}

	public Mesh getMesh() {
		return mesh;
	}

}
