package lu.pcy113.pdr.engine.utils;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.geom.ObjLoader;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;

public final class SceneLoader {
	
	public Scene loadScene(String name, String filePath) {
		Scene scene = null;
		
		String lines = FileUtils.readFile(filePath);
		String firstLine = lines.split("\n", 1)[0];
		if(!firstLine.startsWith("scene"))
			return null;
		
		String type = firstLine.split(" ")[1];
		switch (type.toLowerCase()) {
		case "3d":
			scene = loadScene3D(name, lines);
			break;
		case "2d":
			scene = loadScene2D(name, lines);
			break;
		}
		
		return scene;
	}
	
	public Scene3D load3DScene(String name, String filePath) {
		return loadScene3D(name, FileUtils.readFile(filePath));
	}
	
	private Scene3D loadScene3D(String name, String txt) {
		String firstLine = txt.split("\n", 1)[0];
		if(!firstLine.startsWith("scene"))
			return null;
		if(!firstLine.split(" ")[1].toLowerCase().equals("3d"))
			return null;
		Scene3D scene = new Scene3D(name);
		CacheManager cache = new CacheManager();
		
		scene.addEntity(firstLine, null);
		
		String[] lines = txt.split("\n");
		int li = 0;
		while(li < lines.length) {
			String line = lines[li++];
			String[] tokens = line.split("\\s+");
			
			switch(tokens[0].toLowerCase()) {
			case "mesh":
				if(tokens.length < 4)
					break;
				cache.addMesh(ObjLoader.loadMesh(tokens[1], tokens[2], tokens[3]));
				break;
			case "material":
				if(tokens.length < 4)
					break;
				break;
			}
		}
		
		return null;
	}
	
	// TODO
	public Scene load2DScene(String name, String filePath) {
		return loadScene2D(name, FileUtils.readFile(filePath));
	}
	
	// TODO
	private Scene loadScene2D(String name, String txt) {
		return null;
	}
	
}
