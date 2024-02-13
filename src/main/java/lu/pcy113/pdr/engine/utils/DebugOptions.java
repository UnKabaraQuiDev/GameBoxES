package lu.pcy113.pdr.engine.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pclib.Pair;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.anim.skeletal.Joint;
import lu.pcy113.pdr.engine.anim.skeletal.MeshSkeletalAnimation;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.ObjLoader;
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
		RenderShader debShader = deb.getShader();

		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		if (modelMatrix != null)
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, modelMatrix);
		else
			deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, new Matrix4f());
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
	public Vector4f wireframeColor = new Vector4f(1, 0, 0, 1), bonesColor = new Vector4f(0, 1, 0, 1);

	public void wireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f transformationMatrix) {
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
		RenderShader debShader = deb.getShader();
		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		deb.setPropertyIfPresent(WireframeShader.COLOR, wireframeColor);
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);

		GL40.glDrawElements(GL40.GL_TRIANGLES, mesh.getIndicesCount(), GL40.GL_UNSIGNED_INT, 0);

		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
	}
	
	public void bonesWireframe(CacheManager cache, Scene scene, MeshSkeletalAnimation msa, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f transformationMatrix) {
		if (!bones)
			return;
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_POINT);
		Material deb = cache.getMaterial(WireframeMaterial.NAME);
		if (deb == null) {
			WireframeShader shader = new WireframeShader();
			cache.addShader(shader);
			deb = new WireframeMaterial(shader);
			cache.addMaterial(deb);
		}
		RenderShader debShader = deb.getShader();
		debShader.bind();

		deb.setPropertyIfPresent(RenderShader.PROJECTION_MATRIX, projectionMatrix);
		deb.setPropertyIfPresent(RenderShader.VIEW_MATRIX, viewMatrix);
		deb.setPropertyIfPresent(RenderShader.TRANSFORMATION_MATRIX, transformationMatrix);
		deb.setPropertyIfPresent(WireframeShader.COLOR, bonesColor);
		deb.bindProperties(cache, scene, debShader);

		if (GameEngine.DEBUG.ignoreDepth)
			GL40.glDisable(GL40.GL_DEPTH_TEST);
		
		drawBone(msa.getRoot(), new Vector3f(0));
		
		GL40.glPolygonMode(GL40.GL_FRONT_AND_BACK, GL40.GL_FILL);
		GL40.glEnable(GL40.GL_DEPTH_TEST);
	}
	
	public void drawBone(Joint bone, Vector3f parentPos) {
		// Calculate the position of the bone's head in world space
		Vector3f headPos = new Vector3f(bone.getAnimatedTransform().m03(), bone.getAnimatedTransform().m13(), bone.getAnimatedTransform().m23());
		
		System.out.println("drawing bone: " + bone.name);
		
		// Draw a line from the parent bone's head to the current bone's head
		drawLine(parentPos, headPos);
		
		// Recursively draw children bones
		for (Joint child : bone.children) {
			drawBone(child, headPos);
		}
	}

	// Method to draw a line between two points
	private void drawLine(Vector3f start, Vector3f end) {
		 GL40.glBegin(GL40.GL_LINES);
		 GL40.glVertex3f(start.x, start.y, start.z);
		 GL40.glVertex3f(end.x, end.y, end.z);
		 GL40.glEnd();
	}

	public void pointWireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f transformationMatrix) {
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
		RenderShader debShader = deb.getShader();
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
	
	private FileWriter eventFileWriter;
	private HashMap<String, Pair<Long, Long>> statuses = new HashMap<>();
	
	public DebugOptions() {
		try {
			this.eventFileWriter = new FileWriter(FileUtils.appendName(GlobalLogger.getLogger().getLogFile().getPath(), "-time"));
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
			if(status == null)
				return;
			eventFileWriter.append("start>"+type+":"+status.getKey()+":"+status.getValue()+"/end>"+System.currentTimeMillis()+":"+System.nanoTime()+"\n");
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
