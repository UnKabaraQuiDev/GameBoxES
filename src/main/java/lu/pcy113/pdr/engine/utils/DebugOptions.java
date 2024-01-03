package lu.pcy113.pdr.engine.utils;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.ObjLoader;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.material.wireframe.WireframeMaterial;
import lu.pcy113.pdr.engine.graph.material.wireframe.WireframeShader;
import lu.pcy113.pdr.engine.scene.Scene;

public class DebugOptions {

	public boolean ignoreDepth = true;

	public boolean gizmos = true;
	public Gizmo gizmoXYZ;

	public void gizmos(CacheManager cache, Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix, Object modelMatrix) {
		if (!gizmos)
			return;

		if (gizmoXYZ == null) {
			gizmoXYZ = ObjLoader.loadGizmo("named_xyz", "./resources/models/gizmos/named_XYZ.obj");
		}

		gizmoXYZ.bind();

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);

		GizmoMaterial deb;
		if (cache.hasMaterial(GizmoShader.GizmoMaterial.NAME)) {
			deb = (GizmoMaterial) cache.getMaterial(GizmoShader.GizmoMaterial.NAME);
		} else {
			deb = (GizmoMaterial) cache.loadMaterial(GizmoShader.GizmoMaterial.class);
		}
		Shader debShader = deb.getShader();

		debShader.bind();

		deb.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		deb.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		if (modelMatrix != null)
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, modelMatrix);
		else
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, new Matrix4f());
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glLineWidth(2.5f);
		GL40.glDrawElements(GL40.GL_LINES, gizmoXYZ.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		gizmoXYZ.unbind();
	}

	public boolean wireframe = true;
	public Vector4f wireframeColor = new Vector4f(1, 0, 0, 1);

	public void wireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix, Object modelMatrix) {
		if (!wireframe)
			return;

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);
		Material deb = cache.getMaterial(WireframeMaterial.NAME);
		if (deb == null) {
			WireframeShader shader = new WireframeShader();
			cache.addShader(shader);
			deb = new WireframeMaterial(shader);
			cache.addMaterial(deb);
		}
		Shader debShader = deb.getShader();
		debShader.bind();

		deb.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		deb.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		if (modelMatrix != null)
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, modelMatrix);
		else
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, new Matrix4f());
		deb.setProperty(WireframeShader.COLOR, wireframeColor);
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
	}

	public void pointWireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
		if (!wireframe)
			return;

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_POINT);
		Material deb = cache.getMaterial(WireframeMaterial.NAME);
		if (deb == null) {
			WireframeShader shader = new WireframeShader();
			cache.addShader(shader);
			deb = new WireframeMaterial(shader);
			cache.addMaterial(deb);
		}
		Shader debShader = deb.getShader();
		debShader.bind();

		deb.setProperty(Shader.PROJECTION_MATRIX, projectionMatrix);
		deb.setProperty(Shader.VIEW_MATRIX, viewMatrix);
		if (modelMatrix != null)
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, modelMatrix);
		else
			deb.setProperty(Shader.TRANSFORMATION_MATRIX, new Matrix4f());
		deb.setProperty(WireframeShader.COLOR, wireframeColor);
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glDrawElements(GL40.GL_POINTS, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
	}

}
