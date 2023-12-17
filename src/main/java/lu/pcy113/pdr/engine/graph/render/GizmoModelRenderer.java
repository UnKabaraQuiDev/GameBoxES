package lu.pcy113.pdr.engine.graph.render;

import java.util.List;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.entity.Component;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.GizmoModelComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TransformComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;

public class GizmoModelRenderer extends Renderer<Scene, GizmoModelComponent> {

	public GizmoModelRenderer() {
		super(GizmoModel.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, GizmoModelComponent m) {
		GizmoModel model = m.getGizmoModel(cache);
		if (model == null)
			return;

		GlobalLogger.log(Level.INFO, "GizmoModel : " + model.getId());

		Gizmo gizmo = cache.getGizmo(model.getGizmo());
		if (gizmo == null)
			return;
		gizmo.bind();

		Material material = cache.getMaterial(GizmoMaterial.NAME);
		if (material == null) {
			GizmoShader shader = new GizmoShader();
			cache.addShader(shader);
			material = new GizmoMaterial(shader);
			cache.addMaterial(material);
		}
		Shader shader = cache.getShader(material.getShader());
		if (shader == null)
			return;

		shader.bind();

		Matrix4f projectionMatrix = null, viewMatrix = null;
		Entity parent = m.getParent();
		List<Class<? extends Component>> transforms = parent.getComponents(TransformComponent.class);
		// TransformComponent transform = null;
		Object transformationMatrix = null;
		if (!transforms.isEmpty())
			transformationMatrix = ((TransformComponent) parent.getComponent(transforms.get(0))).getTransform()
					.getMatrix();
		else
			transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
			viewMatrix = scene.getCamera().getViewMatrix();
			material.setPropertyIfPresent(Shader.PROJECTION_MATRIX, projectionMatrix);
			material.setPropertyIfPresent(Shader.VIEW_MATRIX, viewMatrix);
			material.setPropertyIfPresent(Shader.TRANSFORMATION_MATRIX, transformationMatrix);
			material.setPropertyIfPresent(Shader.VIEW_POSITION, ((Camera3D) scene.getCamera()).getPosition());
		}

		material.bindProperties(cache, scene, shader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

		GL11.glLineWidth(model.getLineWidth());
		GL11.glDrawElements(GL11.GL_LINES, gizmo.getIndicesCount(), GL11.GL_UNSIGNED_INT, 0);

		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		// GL40.glEnable(GL40.GL_DEPTH_TEST);

		gizmo.unbind();
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
