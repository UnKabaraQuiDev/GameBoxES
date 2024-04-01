package lu.kbra.gamebox.client.es.game.game.render.shaders;

import org.joml.Vector2f;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.impl.shader.AbstractShaderPart;

public class UISliderShader extends RenderShader {

	public static final String NAME = UISliderShader.class.getName();

	public static final String PROGRESS = "progress";
	public static final String BAR_SIZE = "barSize";
	public static final String SLIDER_SIZE = "sliderSize";
	public static final String MESH_SIZE = "meshSize";
	public static final String FIRST_COLOR = "firstColor";
	public static final String LAST_COLOR = "lastColor";
	public static final String BORDER_THICKNESS = "borderThickness";
	public static final String FG_COLOR = "fgColor";
	public static final String BG_COLOR = "bgColor";
	public static final String SCALE_FACTOR = "scaleFactor";

	public UISliderShader() {
		super(NAME, true, AbstractShaderPart.load("./resources/shaders/plain.vert"), AbstractShaderPart.load("./resources/shaders/ui/slider.frag"));
	}

	@Override
	public void createUniforms() {
		createSceneUniforms();

		createUniform(PROGRESS);
		createUniform(BAR_SIZE);
		createUniform(SLIDER_SIZE);
		createUniform(MESH_SIZE);
		createUniform(FIRST_COLOR);
		createUniform(LAST_COLOR);
		createUniform(BORDER_THICKNESS);
		createUniform(FG_COLOR);
		createUniform(BG_COLOR);
		createUniform(SCALE_FACTOR);
	}

	public static class UISliderMaterial extends Material {

		public static final String NAME = UISliderMaterial.class.getName();

		private float progress, borderThickness, scaleFactor;
		private Vector2f meshSize, barSize, sliderSize;
		private Vector4f firstColor, lastColor, fgColor, bgColor;

		public UISliderMaterial(
				Float prog,
				Vector2f ms, Vector2f bs, Vector2f ss,
				Vector4f fCol, Vector4f lCol,
				Float bTh,
				Vector4f fgCol, Vector4f bgCol,
				Float sf) {
			this(new UISliderShader(), prog, ms, bs, ss, fCol, lCol, bTh, fgCol, bgCol, sf);
		}

		public UISliderMaterial(
				RenderShader shader,
				float prog,
				Vector2f ms, Vector2f bs, Vector2f ss,
				Vector4f fCol, Vector4f lCol,
				float bTh,
				Vector4f fgCol, Vector4f bgCol,
				float sf) {
			super(NAME, shader);

			setMeshSize(ms);
			setProgress(prog);
			setBarSize(bs);
			setSliderSize(ss);
			setFirstColor(fCol);
			setLastColor(lCol);
			setBorderThickness(bTh);
			setFgColor(fgCol);
			setBgColor(bgCol);
			setScaleFactor(sf);
		}
		
		public void setScaleFactor(float scaleFactor) {
			this.scaleFactor = scaleFactor;
			setPropertyIfPresent(SCALE_FACTOR, scaleFactor);
		}

		public void setFgColor(Vector4f fgColor) {
			this.fgColor = fgColor;
			setPropertyIfPresent(FG_COLOR, fgColor);
		}

		public void setBgColor(Vector4f bgColor) {
			this.bgColor = bgColor;
			setPropertyIfPresent(BG_COLOR, bgColor);
		}

		public void setBorderThickness(float borderThickness) {
			this.borderThickness = borderThickness;
			setPropertyIfPresent(BORDER_THICKNESS, borderThickness);
		}

		public void setFirstColor(Vector4f firstColor) {
			this.firstColor = firstColor;
			setPropertyIfPresent(FIRST_COLOR, firstColor);
		}

		public void setLastColor(Vector4f lastColor) {
			this.lastColor = lastColor;
			setPropertyIfPresent(LAST_COLOR, lastColor);
		}

		public void setMeshSize(Vector2f meshSize) {
			this.meshSize = meshSize;
			setPropertyIfPresent(MESH_SIZE, meshSize);
		}

		public void setProgress(float progress) {
			this.progress = progress;
			setPropertyIfPresent(PROGRESS, progress);
		}

		public void setBarSize(Vector2f barSize) {
			this.barSize = barSize;
			setPropertyIfPresent(BAR_SIZE, barSize);
		}

		public void setSliderSize(Vector2f sliderSize) {
			this.sliderSize = sliderSize;
			setPropertyIfPresent(SLIDER_SIZE, sliderSize);
		}

	}

}
