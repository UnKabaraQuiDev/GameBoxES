package lu.kbra.gamebox.client.es.game.game.debug.shaders;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class JoystickStateShader extends RenderShader {

	public static final String NAME = JoystickStateShader.class.getName();

	public static final String COLOR = "color";
	public static final String RADIUS = "radius";
	public static final String POSITION = "position";
	public static final String THRESHOLD = "threshold";
	public static final String BUTTON = "button";

	public JoystickStateShader() {
		super(NAME, AbstractShaderPart.load("./resources/shaders/ui/joystick.frag"), AbstractShaderPart.load("./resources/shaders/ui/plain.vert"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(COLOR);
		createUniform(RADIUS);
		createUniform(POSITION);
		createUniform(THRESHOLD);
		createUniform(BUTTON);
	}

	public static class JoystickStateMaterial extends Material {

		public static final String NAME = JoystickStateMaterial.class.getName();

		private Vector4f color;
		private float radius, threshold, button;
		private Vector2f position;

		public JoystickStateMaterial() {
			super(NAME, new JoystickStateShader());
		}

		public JoystickStateMaterial(JoystickStateShader shader) {
			super(NAME, shader);
		}

		@Override
		public void bindProperties(CacheManager cache, Renderable parent, RenderShader shader) {
			setProperty(COLOR, color);
			setProperty(RADIUS, radius);
			setProperty(POSITION, position);
			setProperty(THRESHOLD, threshold);
			setProperty(BUTTON, button);

			super.bindProperties(cache, parent, shader);
		}

		public void setColor(Vector4f color) {
			this.color = color;
		}

		public void setRadius(float radius) {
			this.radius = radius;
		}

		public void setPosition(Vector2f position) {
			this.position = position;
		}

		public void setThreshold(float threshold) {
			this.threshold = threshold;
		}

		public void setButton(float button) {
			this.button = button;
		}

	}

}