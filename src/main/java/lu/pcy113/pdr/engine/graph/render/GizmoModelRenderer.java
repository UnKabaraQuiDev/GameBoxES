package lu.pcy113.pdr.engine.graph.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.scene.Camera3D;
import lu.pcy113.pdr.engine.scene.Scene;

public class GizmoModelRenderer extends Renderer<Scene, GizmoModel> {

	public GizmoModelRenderer() {
		super(GizmoModel.class);
	}
	
	@Override
	public void render(CacheManager cache, Scene scene, GizmoModel model) {
		System.out.println("Gizmo : "+model.getId());
		
		Gizmo gizmo = cache.getGizmo(model.getGizmo());
		if(gizmo == null)
			return;
		gizmo.bind();
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);
		Material material = cache.getMaterial(GizmoMaterial.NAME);
		if(material == null) {
			material = new GizmoMaterial();
			cache.addShader(new GizmoShader());
			cache.addMaterial(material);
		}
		Shader shader = cache.getShader(material.getShader());
		
		shader.bind();
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		material.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		material.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		material.setProperty(Shader.TRANSFORMATION_MATRIX, model.getTransform().getMatrix());
		//((Camera3D) scene.getCamera()).updateMatrix();
		material.setProperty(Shader.VIEW_POSITION, ((Camera3D) scene.getCamera()).getPosition());
		
		//Logger.log("cam: "+((Camera3D) scene.getCamera()).getPosition());
		
		material.bindProperties(cache, scene, shader);
		
		/*if(GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);*/
		
		GL40.glLineWidth(model.getLineWidth());
		GL40.glDrawElements(GL40.GL_LINES, gizmo.getVertexCount(), GL40.GL_UNSIGNED_INT, 0);
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		//GL40.glEnable(GL40.GL_DEPTH_TEST);
		
		gizmo.unbind();
	}
	
	@Override
	public void cleanup() {}

}
