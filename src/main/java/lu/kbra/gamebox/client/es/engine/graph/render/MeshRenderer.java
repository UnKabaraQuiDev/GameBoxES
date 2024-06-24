package lu.kbra.gamebox.client.es.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengles.GLES30;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.graph.shader.RenderShader;
import lu.kbra.gamebox.client.es.engine.objs.entity.Entity;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.MeshComponent;
import lu.kbra.gamebox.client.es.engine.objs.entity.components.TransformComponent;
import lu.kbra.gamebox.client.es.engine.scene.Scene;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera;
import lu.kbra.gamebox.client.es.engine.scene.camera.Camera3D;

public class MeshRenderer extends Renderer<Scene, MeshComponent> {

	public MeshRenderer() {
		super(Mesh.class);
	}

	@Override
	public void render_in(CacheManager cache, Scene scene, MeshComponent m) {
		Entity e = m.getParent();

		Mesh mesh = m.getMesh(cache);
		if (mesh == null) {
			GlobalLogger.log(Level.WARNING, "Mesh is null, for entity: " + e + "!");
			cache.dump(GlobalLogger.getLogger().getFileWriter());
			return;
		}

		GameEngine.DEBUG.start("r_mesh");

		GlobalLogger.log(Level.FINE, "Mesh : " + mesh.getId() + ", vao:" + mesh.getVao() + ", vec:" + mesh.getVertexCount() + ", vbo:" + mesh.getVbo());

		GameEngine.DEBUG.start("r_mesh_bind");
		mesh.bind();
		GameEngine.DEBUG.end("r_mesh_bind");

		GameEngine.DEBUG.start("r_mesh_ms_lookup");
		Material material = mesh.getMaterial();
		if (material == null) {
			GlobalLogger.log(Level.WARNING, "Material is null!");
			return;
		}
		RenderShader shader = material.getRenderShader();
		if (shader == null) {
			GlobalLogger.log(Level.WARNING, "Shader is null!");
			return;
		}
		GameEngine.DEBUG.end("r_mesh_ms_lookup");

		GameEngine.DEBUG.start("r_shader_bind");
		shader.bind();
		GameEngine.DEBUG.end("r_shader_bind");

		GameEngine.DEBUG.start("r_uniforms");
		GameEngine.DEBUG.start("r_uniforms_scene");
		Matrix4f projectionMatrix = null, viewMatrix = null, transformationMatrix = new Matrix4f().identity();
		if (scene != null) {
			Camera camera = scene.getCamera();
			projectionMatrix = camera.getProjection().getProjectionMatrix();
			viewMatrix = camera.getViewMatrix();
			shader.setUniform(RenderShader.PROJECTION_MATRIX, projectionMatrix);
			shader.setUniform(RenderShader.VIEW_MATRIX, viewMatrix);
			if (camera instanceof Camera3D) {
				material.setPropertyIfPresent(RenderShader.VIEW_POSITION, ((Camera3D) camera).getPosition());
			}
		}
		GameEngine.DEBUG.end("r_uniforms_scene");

		GameEngine.DEBUG.start("r_uniforms_transform");
		if (material.hasProperty(RenderShader.TRANSFORMATION_MATRIX)) {
			if (e.hasComponent(TransformComponent.class)) {
				TransformComponent transform = (TransformComponent) e.getComponent(e.getComponents(TransformComponent.class).get(0));
				if (transform != null) {
					transformationMatrix = transform.getTransform().getMatrix();
				}
				material.setProperty(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
			}
		}
		GameEngine.DEBUG.end("r_uniforms_transform");

		GameEngine.DEBUG.start("r_uniforms_bind");
		material.bindProperties(cache, scene, shader);
		GameEngine.DEBUG.end("r_uniforms_bind");
		GameEngine.DEBUG.end("r_uniforms");

		GameEngine.DEBUG.start("r_blend");
		if (shader.isTransparent()) {
			GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
			GLES30.glEnable(GLES30.GL_BLEND);
		}
		GameEngine.DEBUG.end("r_blend");

		// GLES30.glPolygonMode(shader.getFaceMode().getGlId(), shader.getRenderType().getGlId());

		GameEngine.DEBUG.start("r_draw");
		GLES30.glDrawElements(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GLES30.GL_UNSIGNED_INT, 0);
		GameEngine.DEBUG.end("r_draw");

		GLES30.glDisable(GLES30.GL_BLEND);

		// debug only
		GameEngine.DEBUG.start("r_debug_wf");
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, transformationMatrix);
		GameEngine.DEBUG.end("r_debug_wf");

		mesh.unbind();

		GameEngine.DEBUG.end("r_mesh");

		GameEngine.DEBUG.start("r_debug_gizmo");
		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, transformationMatrix);
		GameEngine.DEBUG.end("r_debug_gizmo");
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
