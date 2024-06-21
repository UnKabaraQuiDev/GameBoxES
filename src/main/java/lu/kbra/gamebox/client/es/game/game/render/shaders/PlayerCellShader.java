package lu.kbra.gamebox.client.es.game.game.render.shaders;

import java.util.HashMap;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.TextureMaterial;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.graph.texture.Texture;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class PlayerCellShader extends RenderShader {

	public static final String NAME = PlayerCellShader.class.getName();

	public static final String TEXTURE = "color";
	public static final String OVERLAY = "overlay";

	public static final String SPEED = "speed";
	public static final String DAMAGE = "damage";
	public static final String PHOTO = "photo";
	public static final String HEALTH = "health";

	public PlayerCellShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/cell/player.frag"), AbstractShaderPart.load("./resources/shaders/cell/player.vert"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(TEXTURE);
		createUniform(OVERLAY);

		createUniform(DAMAGE);
		createUniform(HEALTH);
		createUniform(PHOTO);
		createUniform(SPEED);
	}

	public static class PlayerCellMaterial extends TextureMaterial {

		public static final String PLAYER_TEXTURE_NAME = "player.png";
		public static final String PLAYER_TEXTURE_PATH = "./resources/gd/cells/player/player.png";
		
		public static final String PLAYER_OVERLAY_TEXTURE_NAME = "overlay.png";
		public static final String PLAYER_OVERLAY_TEXTURE_PATH = "./resources/gd/cells/player/overlay.png";

		private int damage = 0, health = 0, photo = 0, speed = 0;

		public PlayerCellMaterial(String name, PlayerCellShader shader, SingleTexture color, SingleTexture overlay) {
			super(name, shader, new HashMap<String, Texture>() {
				{
					put(TEXTURE, color);
					put(OVERLAY, overlay);
				}
			});
		}

		public PlayerCellMaterial(String name, SingleTexture color, SingleTexture overlay) {
			this(name, new PlayerCellShader(), color, overlay);
		}
		
		@Override
		public void bindProperties(CacheManager cache, Renderable scene, RenderShader shader) {
			super.setProperty(DAMAGE, damage);
			super.setProperty(HEALTH, health);
			super.setProperty(PHOTO, photo);
			super.setProperty(SPEED, speed);
			
			super.bindProperties(cache, scene, shader);
		}

		public int getDamage() {
			return damage;
		}

		public void setDamage(int damage) {
			this.damage = damage;
		}

		public int getHealth() {
			return health;
		}

		public void setHealth(int health) {
			this.health = health;
		}

		public int getPhoto() {
			return photo;
		}

		public void setPhoto(int photo) {
			this.photo = photo;
		}

		public int getSpeed() {
			return speed;
		}

		public void setSpeed(int speed) {
			this.speed = speed;
		}

	}

}
