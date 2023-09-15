package lu.pcy113.pdr.engine.utils;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.IntAttribArray;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.graph.material.wireframe.WireframeMaterial;
import lu.pcy113.pdr.engine.graph.material.wireframe.WireframeShader;
import lu.pcy113.pdr.engine.scene.Scene;

public class DebugOptions {
	
	public boolean ignoreDepth = true;
	
	public boolean gizmos = true;
	public Gizmo gizmoXYZ;
	
	public void gizmos(CacheManager cache, Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
		if(!gizmos)
			return;
		
		if(gizmoXYZ == null) {
			gizmoXYZ = new Gizmo(
					"xyz",
					new FloatAttribArray("pos", 0, 3, new float[] {
							// x
							0, 0, 0,
							1, 0, 0,
							// y
							0, 0, 0,
							0, 1, 0,
							// z
							0, 0, 0,
							0, 0, 1
					}),
					new IntAttribArray("ind", -1, 1, new int[] {
							0, 1, // x
							2, 3, // y
							4, 5  // z
					}),
					new FloatAttribArray("col", 1, 4, new float[] {
							// x
							1, 0, 0, 1,
							1, 0, 0, 1,
							// y
							0, 1, 0, 1,
							0, 1, 0, 1,
							// z
							0, 0, 1, 1,
							0, 0, 1, 1,
					}));
		}
		
		gizmoXYZ.bind();
		
		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
		Material deb = cache.getMaterial(GizmoMaterial.NAME);
		if(deb == null) {
			deb = new GizmoMaterial();
			cache.addShader(new GizmoShader());
			cache.addMaterial(deb);
		}
		Shader debShader = cache.getShader(deb.getShader());
		debShader.bind();
		
		deb.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		deb.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		if(modelMatrix != null)
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, modelMatrix);
		else
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, new Matrix4f());
		deb.bindProperties(cache, scene, debShader);
		
		if(GameEngine.DEBUG.ignoreDepth)
			GL30.glDisable(GL30.GL_DEPTH_TEST);
		
		GL30.glLineWidth(2.5f);
		GL30.glDrawElements(GL30.GL_LINES, gizmoXYZ.getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
		
		GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
		GL30.glEnable(GL30.GL_DEPTH_TEST);
		
		gizmoXYZ.unbind();
	}
	
	public boolean wireframe = true;
	
	public void wireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
		if(GameEngine.DEBUG.wireframe) {
			GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
			Material deb = cache.getMaterial(WireframeMaterial.NAME);
			if(deb == null) {
				deb = new WireframeMaterial();
				cache.addShader(new WireframeShader());
				cache.addMaterial(deb);
			}
			Shader debShader = cache.getShader(deb.getShader());
			debShader.bind();
			
			deb.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
			deb.setProperty(Shader.VIEW_MATRIX, viewMatrix);
			if(modelMatrix != null)
				deb.setProperty(Shader.TRANSFORMATION_MATRIX, modelMatrix);
			else
				deb.setProperty(Shader.TRANSFORMATION_MATRIX, new Matrix4f());
			deb.bindProperties(cache, scene, debShader);
			
			if(GameEngine.DEBUG.ignoreDepth)
				GL30.glDisable(GL30.GL_DEPTH_TEST);
			
			GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getVertexCount(), GL30.GL_UNSIGNED_INT, 0);
			
			GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_FILL);
			GL30.glEnable(GL30.GL_DEPTH_TEST);
		}
	}
	
}
