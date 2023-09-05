package lu.pcy113.pdr.engine.utils;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.material.ShaderPart;
import lu.pcy113.pdr.engine.scene.Scene;

public class DebugOptions {
	
	public boolean wireframe = false;
	public String material = "debug";
	public String shader = "debug_shader";
	public boolean ignoreDepth = true;
	
	public void wireframe(CacheManager cache, Scene scene, Mesh mesh, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f modelMatrix) {
		if(GameEngine.DEBUG.wireframe) {
			GL30.glPolygonMode(GL30.GL_FRONT_AND_BACK, GL30.GL_LINE);
			Material deb = cache.getMaterial(GameEngine.DEBUG.material);
			if(deb == null) {
				Shader matSh = new Shader(GameEngine.DEBUG.shader,
						new ShaderPart("./resources/shaders/debug/debug.vert"),
						new ShaderPart("./resources/shaders/debug/debug.frag")) {
					@Override
					public void createUniforms() {
						getUniform(Shader.PROJECTION_MATRIX);
						getUniform(Shader.VIEW_MATRIX);
						getUniform(Shader.TRANSFORMATION_MATRIX);
					}
				};
				cache.addShader(matSh);
				deb = new Material(GameEngine.DEBUG.material, null, null, matSh.getId());
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
