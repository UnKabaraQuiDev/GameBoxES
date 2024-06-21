package lu.kbra.gamebox.client.es.game.game.utils.global;

import org.joml.Vector4f;

public final class GlobalConsts {

	public static final String TEXT_TEXTURE = "text-30px";

	public static final Vector4f PRIMARY_DARK = new Vector4f(86f, 159f, 217f, 255f).div(255f);
	public static final Vector4f PRIMARY_LIGHT = new Vector4f(153f, 198f, 233f, 255f).div(255f);
	public static final Vector4f SECONDARY_DARK = new Vector4f(76f, 199f, 119f, 255f).div(255f);
	public static final Vector4f SECONDARY_LIGHT = new Vector4f(160f, 232f, 153f, 255f).div(255f);
	public static final Vector4f HIGHLIGHT = new Vector4f(255f, 126f, 0f, 255f).div(255f);
	public static final Vector4f BG = new Vector4f(70f, 70f, 70f, 255f).div(255f);
	public static final Vector4f TRANS_BG = new Vector4f(70f, 70f, 70f, 200f).div(255f);
	public static final Vector4f NEUTRAL = new Vector4f(255, 255, 255, 255);

	public static final float WORLD_BG_HEIGHT = -1f;
	public static final float PLANTS_HEIGHT = -0.9f;
	public static final float ENNEMY_CELLS_HEIGHT = -0.8f;
	public static final float PLAYER_CELL_HEIGHT = -0.1f;
	public static final float TOXINS_HEIGHT = 0f;
	
	public static final float UI_BG_HEIGHT = 0.8f;
	public static final float UI_COMPONENTS_HEIGHT = 0.8f;
	public static final float UI_OVER_COMPONENTS_HEIGHT = 0.8f;

}
