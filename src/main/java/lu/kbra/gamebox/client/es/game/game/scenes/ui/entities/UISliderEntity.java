package lu.kbra.gamebox.client.es.game.game.scenes.ui.entities;

import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.Transform3DComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.UIComponent;
import lu.kbra.gamebox.client.es.engine.utils.geo.GeoPlane;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform3D;
import lu.kbra.gamebox.client.es.game.game.render.shaders.UISliderShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.UISliderShader.UISliderMaterial;

public class UISliderEntity extends Entity {

	private Mesh mesh;

	public UISliderEntity(CacheManager cache, Vector2f size, Vector2f values, float step, float _default, Transform3D transform) {
		UISliderMaterial mat = (UISliderMaterial) cache.loadOrGetMaterial(UISliderShader.UISliderMaterial.NAME, UISliderShader.UISliderMaterial.class, 0.1f, new Vector2f(size), new Vector2f(1, 0.5f), new Vector2f(0.1f, 1), new Vector4f(0, 1, 0, 1),
				new Vector4f(1, 0, 0.2f, 1), 0.01f, new Vector4f(1, 1, 1, 1), new Vector4f(0), 1.05f);
		mesh = Mesh.newQuad("uiSliderEntityMesh" + hashCode(), mat, new Vector2f(size).div(1.05f));
		cache.addMesh(mesh);
		addComponent(new MeshComponent(mesh));

		addComponent(new UISliderComponent(_default, step, new Vector2f(size), values, new Vector2f(0.1f, 1)));

		addComponent(new Transform3DComponent(transform));
	}

	public class UISliderComponent extends UIComponent {

		private float progress, step;
		private Vector2f meshSize, values, sliderSize;

		public UISliderComponent(float progress, float step, Vector2f meshSize, Vector2f values, Vector2f sliderSize) {
			this.progress = progress;
			this.step = step;
			this.sliderSize = sliderSize;
			this.meshSize = meshSize;
			this.values = values;
		}

		@Override
		public boolean contains(Vector2f point) {
			point = new Vector2f(point.y, point.x);
			return point.x > -meshSize.x / 2 && point.y > -meshSize.y / 2 && point.x < meshSize.x / 2 && point.y < meshSize.y / 2;
		}

		@Override
		public void hover(Vector2f point) {
			point = new Vector2f(point.y, point.x);
			System.err.println("point1: "+point);
			System.err.println("point2: "+point.add(meshSize.mul(progress-0.5f, new Vector2f()).x, 0));
			System.err.println("in rect: "+sliderSize);
			if (point.x > -sliderSize.x / 2 && point.y > -sliderSize.y / 2 && point.x < sliderSize.x / 2 && point.y < sliderSize.y / 2) {
				((UISliderMaterial) mesh.getMaterial()).setFgColor(new Vector4f(1, 0, 0, 1));
			}else {
				((UISliderMaterial) mesh.getMaterial()).setFgColor(new Vector4f(1, 1, 1, 1));
			}
		}

		@Override
		public void click(Vector2f pos) {
			pos = new Vector2f(pos.y, pos.x);

			Vector3f parentPos = getParent().getComponent(Transform3DComponent.class).getTransform().getTranslation();
			pos.sub(GeoPlane.YZ.projectToPlane(parentPos));

			progress = 1 - (pos.x + meshSize.x / 2) / meshSize.x;

			((UISliderMaterial) mesh.getMaterial()).setProgress(progress);
		}

		public float getValue() {
			return Math.lerp(values.x, values.y, progress);
		}

	}

}
