package lu.pcy113.pdr.engine.graph.render;

import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.skeletal.ArmatureAnimation;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.objs.entity.Entity;
import lu.pcy113.pdr.engine.objs.entity.components.ArmatureAnimationComponent;
import lu.pcy113.pdr.engine.objs.entity.components.MeshComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TransformComponent;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.camera.Camera;
import lu.pcy113.pdr.engine.scene.camera.Camera3D;

public class MeshRenderer extends Renderer<Scene, MeshComponent> {

	public MeshRenderer() {
		super(Mesh.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, MeshComponent m) {
		Entity e = m.getParent();

		Mesh mesh = m.getMesh(cache);
		if (mesh == null) {
			GlobalLogger.log(Level.WARNING, "Mesh is null!");
			return;
		}

		GameEngine.DEBUG.start("r_mesh");

		GlobalLogger.log(Level.INFO, "Mesh : " + mesh.getId() + ", vao:" + mesh.getVao() + ", vec:" + mesh.getVertexCount() + ", vbo:" + mesh.getVbo());

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

		GameEngine.DEBUG.start("r_uniforms_skelet");
		if (e.hasComponent(ArmatureAnimationComponent.class)) {
			ArmatureAnimationComponent msac = (ArmatureAnimationComponent) e.getComponent(e.getComponents(ArmatureAnimationComponent.class).get(0));
			if (msac != null) {
				ArmatureAnimation msa = msac.getArmatureAnimation();
				msa.bind(shader);
			}
		}
		GameEngine.DEBUG.end("r_uniforms_skelet");

		GameEngine.DEBUG.start("r_uniforms_bind");
		material.bindProperties(cache, scene, shader);
		GameEngine.DEBUG.end("r_uniforms_bind");
		GameEngine.DEBUG.end("r_uniforms");

		GameEngine.DEBUG.start("r_blend");
		if (shader.isTransparent()) {
			GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
			GL40.glEnable(GL40.GL_BLEND);
		}
		GameEngine.DEBUG.end("r_blend");

		GL40.glPolygonMode(shader.getFaceMode().getGlId(), shader.getRenderType().getGlId());

		GameEngine.DEBUG.start("r_draw");
		if (mesh.hasDrawBuffer()) {
			mesh.getDrawBuffer().bind();
			GL40.glDrawElementsIndirect(shader.getBeginMode().getGlId(), GL40.GL_UNSIGNED_INT, 0);
		} else {
			GL40.glDrawElements(shader.getBeginMode().getGlId(), mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);
		}
		GameEngine.DEBUG.end("r_draw");

		GL40.glDisable(GL40.GL_BLEND);

		// debug only
		GameEngine.DEBUG.start("r_debug_wf");
		GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, transformationMatrix);
		GameEngine.DEBUG.end("r_debug_wf");

		mesh.unbind();

		GameEngine.DEBUG.end("r_mesh");

		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, transformationMatrix);

		/*
		 * if(e.hasComponent(MeshSkeletalAnimationComponent.class)) {
		 * GameEngine.DEBUG.bonesWireframe(cache, scene,
		 * ((MeshSkeletalAnimationComponent)
		 * e.getComponent(MeshSkeletalAnimationComponent.class)).
		 * getMeshSkeletalAnimation(), projectionMatrix, viewMatrix,
		 * transformationMatrix); }
		 */
	}

	@Override
	public void cleanup() {
		super.cleanup();
	}

}
