package lu.pcy113.pdr.engine.graph.render;

import java.util.Arrays;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.text.TextMesh;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.text.TextMaterial;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.objs.entity.components.PointLightSurfaceComponent;
import lu.pcy113.pdr.engine.objs.entity.components.TextModelComponent;
import lu.pcy113.pdr.engine.objs.text.TextModel;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;
import lu.pcy113.pdr.utils.Logger;

public class TextModelRenderer extends Renderer<Scene, TextModelComponent> {
	
	public static final String TEXT_MESH = TextMesh.NAME;
	
	public TextModelRenderer() {
		super(TextModel.class);
	}

	@Override
	public void render(CacheManager cache, Scene scene, TextModelComponent t) {
		Mesh mesh = cache.getMesh(TEXT_MESH+"_"+t.getTextSize());
		if(mesh == null)
			return;
		
		TextModel tModel = t.getTextModel(cache);
		if(tModel == null)
			return;
		
		Logger.log(Level.INFO, "TextModel : m:"+mesh.getId()+" t:"+tModel.getText());
		
		mesh.bind();
		System.err.println("mesh bound");
		
		TextMaterial material = (TextMaterial) cache.getMaterial(tModel.getMaterial()); //cache.getMaterial(mesh.getMaterial());
		if(material == null)
			return;
		System.err.println("corr mat");
		TextShader shader = (TextShader) cache.getShader(material.getShader());
		if(shader == null)
			return;
		System.err.println("corr shader");
		
		shader.bind();
		System.err.println("shader bound");
		
		Matrix4f projectionMatrix = scene.getCamera().getProjection().getProjMatrix();
		Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
		material.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		material.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		material.setProperty(Shader.TRANSFORMATION_MATRIX, tModel.getTransform().getMatrix());
		
		if(scene instanceof Scene3D) {
			PointLightSurfaceComponent plsc = t.getParent().getComponent(PointLightSurfaceComponent.class);
			if(plsc != null)
				plsc.bindLights(cache, ((Scene3D) scene).getLights(), material);
		}
		
		tModel.bindText(cache, mesh.getVertices(), material);
		System.err.println("text bound: "+Arrays.toString(mesh.getVertices().getData()));
		
		material.bindProperties(cache, scene, shader);
		System.err.println("mat props bound");
		
		GL40.glEnable(GL40.GL_BLEND);
		GL40.glBlendFunc(GL40.GL_SRC_ALPHA, GL40.GL_ONE_MINUS_SRC_ALPHA);
		GL40.glDrawElements(GL40.GL_POINTS, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);
		GL40.glDisable(GL40.GL_BLEND);
		
		// debug only
		//GameEngine.DEBUG.wireframe(cache, scene, mesh, projectionMatrix, viewMatrix, tModel.getTransform().getMatrix());
		//GameEngine.DEBUG.pointWireframe(cache, scene, mesh, projectionMatrix, viewMatrix, tModel.getTransform().getMatrix());
		
		mesh.unbind();
		GameEngine.DEBUG.gizmos(cache, scene, projectionMatrix, viewMatrix, tModel.getTransform().getMatrix());
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
	}

}