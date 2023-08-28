package lu.pcy113.pdr.engine.scene;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.graph.Mesh;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.utils.Logger;

public class Scene implements Cleanupable {
	
	private Map<String, Mesh> meshMap;
	
	private Projection projection;
	
	public Scene(int w, int h) {
		Logger.log();
		
		meshMap = new HashMap<>();
		projection = new Projection(w, h);
	}
	
	public void addMesh(String meshId, Mesh mesh) {
		Logger.log();
		
		meshMap.put(meshId, mesh);
	}
	
	@Override
	public void cleanup() {
		Logger.log();
		
		meshMap.values().forEach(Mesh::cleanup);
	}
	
	public Map<String, Mesh> getMeshMap() {return meshMap;}
	public Projection getProjection() {return projection;}
	public void setProjection(Projection projection) {this.projection = projection;}
	
	public void resize(int width, int height) {
		Logger.log();
		projection.updateProjMatrix(width, height);
	}
	
}
