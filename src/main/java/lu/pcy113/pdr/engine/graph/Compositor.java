package lu.pcy113.pdr.engine.graph;

import java.util.Arrays;

import org.lwjgl.opengl.GL30;

import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.Window;
import lu.pcy113.pdr.engine.graph.shader.Material;
import lu.pcy113.pdr.engine.graph.shader.ShaderProgram;
import lu.pcy113.pdr.engine.impl.ChildObject;
import lu.pcy113.pdr.engine.scene.geom.Mesh;
import lu.pcy113.pdr.engine.scene.geom.Model;
import lu.pcy113.pdr.engine.utils.ObjLoader;

public class Compositor implements ChildObject<GameEngine> {
	
	private GameEngine engine;
	
	private Model quad;
	
	public Compositor(GameEngine ge) {
		attach(ge);
		
		quad = new Model("composite-plane", Arrays.asList(ObjLoader.loadMesh("plane.obj")), new Material(null, ShaderProgram.create("composite")));
		quad.getTransform().setRotateAlongAxisDeg(1, 0, 0, 90);
		quad.getTransform().setScale(2, 2, 2);
		quad.getTransform().updateMatrix();
		quad.getMaterial().getShader().getUniformsMap().createUniform("render");
	}
	
	public void render(Window window) {
		// createFrameBuffer
		int fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL30.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
		
		// createTextureAttachment
		int texture = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, window.getWidth(), window.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, 0);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, texture, 0);
		
		// createDepthTextureAttachment
		/*int depth = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, depth);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_DEPTH_COMPONENT32, window.getWidth(), window.getHeight(), 0, GL30.GL_DEPTH_COMPONENT, GL30.GL_FLOAT, 0);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_TEXTURE_2D, depth, 0);*/
		
		// createDepthBufferAttachment
		/*int depthB = GL30.glGenRenderbuffers();
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthB);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH_COMPONENT, window.getWidth(), window.getHeight());
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthB);*/
		
		// bindFrameBuffer
		//GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
		
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT | GL30.GL_DEPTH_BUFFER_BIT);
		GL30.glClearColor(0, 0, 0, 0.0f);
		
		engine.getContentRenderer().render(window, engine.getContentScene());
		
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		
		engine.getUiRenderer().render(window, engine.getUiScene());
		
		GL30.glDisable(GL30.GL_BLEND);
		
		// unbindCurrentFrameBuffer
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, fbo);
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
		GL30.glViewport(0, 0, window.getWidth(), window.getHeight());
		
		/*UniformsMap map = quad.bind();
		for(Mesh mesh : quad.getMeshes()) {
			mesh.bind(map);
			map.setUniform("render", 0);
			GL30.glActiveTexture(GL30.GL_TEXTURE0);
			//GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
			//GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 0, 0);
			//GL30.glEnableVertexAttribArray(0);
			//GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
			GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getNumVertices(), GL30.GL_UNSIGNED_INT, 0);
		}*/
		
		GL30.glClear(GL30.GL_DEPTH_BUFFER_BIT | GL30.GL_COLOR_BUFFER_BIT);
		
		UniformsMap uniforms = quad.bind();
		Mesh mesh = quad.getMeshes().get(0);
		mesh.bind(uniforms);
		
		uniforms.setUniform("render", 0);
		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, texture);
		
		GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getNumVertices(), GL30.GL_UNSIGNED_INT, 0);
		
		GL30.glBindVertexArray(0);
		quad.getMaterial().getShader().unbind();
		
		//GL30.glBlitFramebuffer(0, 0, window.getWidth(), window.getHeight(), 0, 0, window.getWidth(), window.getHeight(), GL30.GL_COLOR_BUFFER_BIT, GL30.GL_NEAREST);
		
		GL30.glDeleteFramebuffers(fbo);
		GL30.glDeleteTextures(texture);
		//GL30.glDeleteTextures(depth);
		//GL30.glDeleteRenderbuffers(depthB);
	}
	
	@Override
	public void attach(GameEngine obj) {
		this.engine = obj;
	}
	@Override
	public GameEngine getAttached() {
		return engine;
	}
	
}
