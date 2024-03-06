package lu.pcy113.pdr.engine.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pclib.Pair;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.utils.ObjLoader;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader;
import lu.pcy113.pdr.engine.graph.material.gizmo.GizmoShader.GizmoMaterial;
import lu.pcy113.pdr.engine.graph.material.wireframe.WireframeMaterial;
import lu.pcy113.pdr.engine.graph.material.wireframe.WireframeShader;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.scene.Scene;

public class DebugOptions implements Cleanupable {

	public boolean ignoreDepth = true;

	public boolean gizmos = true;
	public Gizmo gizmoXYZ, gizmoRect;

	public void gizmos(CacheManager cache, Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix,
			Object modelMatrix) {
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
		RenderShader debShader = deb.getRenderShader();

		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		if (modelMatrix != null)
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, modelMatrix);
		else
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, new Matrix4f().identity());
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glLineWidth(2.5f);
		GL40.glDrawElements(GL40.GL_LINES, gizmoXYZ.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		gizmoXYZ.unbind();
	}

	public boolean wireframe = true, bones = true;
	public Vector4f wireframeColor = new Vector4f(1, 0, 0, 1), bonesColor = new Vector4f(0, 1, 0, 1), textBoxColor = new Vector4f(1, 0, 1, 1);

	public void wireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix,
			Matrix4f transformationMatrix) {
		if (!wireframe)
			return;

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);
		Material deb = cache.getMaterial(WireframeMaterial.NAME);
		if (deb == null) {
			WireframeShader shader = new WireframeShader();
			cache.addRenderShader(shader);
			deb = new WireframeMaterial(shader);
			cache.addMaterial(deb);
		}
		RenderShader debShader = deb.getRenderShader();
		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		if (transformationMatrix != null)
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		else
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, new Matrix4f().identity());
		deb.setPropertyIfPresent(WireframeShader.COLOR, wireframeColor);
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
	}

	public void pointWireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix,
			Matrix4f viewMatrix, Matrix4f transformationMatrix) {
		if (!wireframe)
			return;

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_POINT);
		Material deb = cache.getMaterial(WireframeMaterial.NAME);
		if (deb == null) {
			WireframeShader shader = new WireframeShader();
			cache.addRenderShader(shader);
			deb = new WireframeMaterial(shader);
			cache.addMaterial(deb);
		}
		RenderShader debShader = deb.getRenderShader();
		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		deb.setPropertyIfPresent(WireframeShader.COLOR, wireframeColor);
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glDrawElements(GL40.GL_POINTS, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
	}
	
	public void boundingRect(CacheManager cache, Scene scene, Matrix4f projectionMatrix, Matrix4f viewMatrix,
			Matrix4f modelMatrix, Vector2f boxSize) {
		if (!gizmos)
			return;

		if (gizmoRect == null) {
			gizmoRect = Gizmo.newRect("rect", new Vector2f(1), textBoxColor);
		}
		
		gizmoRect.bind();
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_LINE);

		GizmoMaterial deb;
		if (cache.hasMaterial(GizmoShader.GizmoMaterial.NAME)) {
			deb = (GizmoMaterial) cache.getMaterial(GizmoShader.GizmoMaterial.NAME);
		} else {
			deb = (GizmoMaterial) cache.loadMaterial(GizmoShader.GizmoMaterial.class);
		}
		RenderShader debShader = deb.getRenderShader();

		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		if (modelMatrix != null)
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, modelMatrix.get(new Matrix4f()).scale(new Vector3f(boxSize.x, boxSize.y, 1)));
		else
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, new Matrix4f().identity().scale(new Vector3f(boxSize.x, boxSize.y, 1)));
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glLineWidth(2.5f);
		GL40.glDrawElements(GL40.GL_LINES, gizmoRect.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);

		gizmoRect.unbind();
	}

	private FileWriter eventFileWriter;
	private HashMap<String, Pair<Long, Long>> statuses = new HashMap<>();

	public DebugOptions() {
		try {
			this.eventFileWriter = new FileWriter(
					FileUtils.appendName(GlobalLogger.getLogger().getLogFile().getPath(), "-time"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void start(String type) {
		statuses.put(type, new Pair<Long, Long>(System.currentTimeMillis(), System.nanoTime()));
	}

	public synchronized void end(String type) {
		try {
			Pair<Long, Long> status = statuses.remove(type);
			if (status == null)
				return;
			eventFileWriter.append("start>" + type + ":" + status.getKey() + ":" + status.getValue() + "/end>"
					+ System.currentTimeMillis() + ":" + System.nanoTime() + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cleanup() {
		try {
			eventFileWriter.flush();
			eventFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
