package lu.pcy113.pdr.engine.graph;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLUtil;

import com.codedisaster.steamworks.SteamUtils;

import lu.pcy113.pdr.engine.graph.shader.ShaderModuleData;
import lu.pcy113.pdr.engine.graph.shader.ShaderProgram;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.utils.Logger;

public class SceneRenderer implements Cleanupable {
	
	private ShaderProgram shaderProgram;
	private UniformsMap uniformsMap;
	
	public SceneRenderer() {
		Logger.log();
		
		List<ShaderModuleData> shaderModuleDataList = new ArrayList<>();
		shaderModuleDataList.add(new ShaderModuleData("scene.vert", GL30.GL_VERTEX_SHADER));
		shaderModuleDataList.add(new ShaderModuleData("scene.frag", GL30.GL_FRAGMENT_SHADER));
		this.shaderProgram = new ShaderProgram(shaderModuleDataList);
		
		createUniforms();
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		shaderProgram.cleanup();
	}
	
	private void createUniforms() {
		Logger.log();
		
		uniformsMap = new UniformsMap(shaderProgram.getProgramId());
		uniformsMap.createUniform("projectionMatrix");
	}
	
	public void render(Scene scene) {
		Logger.log();
		
		shaderProgram.bind();
		
		uniformsMap.setUniform("projectionMatrix", scene.getProjection().getProjMatrix());
		
		scene.getMeshMap().values().forEach((mesh) -> {
			Logger.log(mesh.getNumVertices()+" mesh "+mesh.getVaoId());
			
			GL30.glBindVertexArray(mesh.getVaoId());
			GL30.glEnableVertexAttribArray(0);
			
			GL30.glDrawElements(GL30.GL_TRIANGLES, mesh.getNumVertices(), GL30.GL_UNSIGNED_INT, 0);
			
			GL30.glDisableVertexAttribArray(0);
		});
		
		GL30.glBindVertexArray(0);
		
		shaderProgram.unbind();
	}
	
}
