package lu.pcy113.pdr.engine.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joml.Vector3f;

import lu.pcy113.pdr.engine.graph.Mesh;

public final class ObjLoader {
	
	public static List<Mesh> loadMesh(String file) {
		String[] lines = FileUtils.getResource("models/"+file).split("\n");
		System.out.println(Arrays.toString(lines));
		int line = 0;
		
		List<Mesh> meshes = new ArrayList<>();
		
		List<Vector3f> positions = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Integer>  indices = new ArrayList<>();
		
		while(line < lines.length) {
			String cl = lines[line];
			line++;
			System.out.println(line+": "+cl);
			
			if(cl.startsWith("#"))
				continue;
			
			String[] tokens = cl.split(" ");
			
			switch(tokens[0]) {
				case "o":
					break;
				case "v":
					positions.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
					break;
				case "vn":
					normals.add(new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
					break;
				case "s":
					// scale ?
					break;
				case "f":
					indices.add(Integer.parseInt(tokens[1].split("/")[0]));
					indices.add(Integer.parseInt(tokens[2].split("/")[0]));
					indices.add(Integer.parseInt(tokens[3].split("/")[0]));
					break;
			}
		}
		meshes.add(new Mesh(
				toFloat(positions),
				toFloat(normals),
				indices.stream().mapToInt(i -> i).toArray()
		));
		
		return meshes;
	}
	
	private static float[] toFloat(List<Vector3f> doub) {
		float[] result = new float[doub.size() *3];
		int i = 0;
		for (Vector3f f : doub) {
			result[i++] = f.x;
			result[i++] = f.y;
			result[i++] = f.z;
		}
		return result;
	}
	
}
