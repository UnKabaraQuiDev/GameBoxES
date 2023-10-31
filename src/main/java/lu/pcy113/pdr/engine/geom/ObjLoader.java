package lu.pcy113.pdr.engine.geom;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec2fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec4fAttribArray;
import lu.pcy113.pdr.engine.utils.FileUtils;

public final class ObjLoader {
	
	public static Gizmo loadGizmo(String name, String path) {
		String[] lines = FileUtils.readFile(path).split("\n");
		
		List<Vector3f> vertices = new ArrayList<>();
		/*List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();*/
		List<Vector2i> edges = new ArrayList<>();
		List<Vector4f> colors = new ArrayList<>();
		
		int li = 0;
		while(li < lines.length) {
			String line = lines[li++];
			String[] tokens = line.split("\\s+");
			
			switch(tokens[0]) {
			case "v":
				vertices.add(new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])
				));
				if(tokens.length > 4)
					colors.add(new Vector4f(
							Float.parseFloat(tokens[4]),
							Float.parseFloat(tokens[5]),
							Float.parseFloat(tokens[6]),
							tokens.length > 7 ? Float.parseFloat(tokens[7]) : 1
					));
				break;
			case "l":
				edges.add(new Vector2i(
						Integer.parseInt(tokens[1]),
						Integer.parseInt(tokens[2])
				));
				break;
			}
		}
		
		if(colors.isEmpty()) {
			colors = null;
		}
		
		List<Integer> indices = new ArrayList<>();
		Vector3f[] verticesArr = new Vector3f[vertices.size()];
		Vector4f[] colorArr = new Vector4f[vertices.size()];
		
		for(int i = 0; i < vertices.size(); i++) {
			Vector3f pos = vertices.get(i);
			
			/*verticesArr[i*3+0] = pos.x;
			verticesArr[i*3+1] = pos.y;
			verticesArr[i*3+2] = pos.z;*/
			verticesArr[i] = pos;
			
			if(colors != null) {
				Vector4f col = colors.get(i);
				/*colorArr[i*4+0] = col.x;
				colorArr[i*4+1] = col.y;
				colorArr[i*4+2] = col.z;
				colorArr[i*4+3] = col.w;*/
				colorArr[i] = col;
			}
		}
		
		for(Vector2i edge : edges) {
			int i1 = edge.x;
			int i2 = edge.y;
			
			indices.add(i1-1);
			indices.add(i2-1);
		}
		int[] indicesArr = indices.stream().mapToInt((v) -> v).toArray();
		
		/*Logger.log(Level.INFO, "Loaded " + name);
		Logger.log(Level.INFO, "Vertices " + Arrays.toString(verticesArr));
		Logger.log(Level.INFO, "Color " + Arrays.toString(colorArr));
		Logger.log(Level.INFO, "Indices " + Arrays.toString(indicesArr));*/
		
		return new Gizmo(
				name,
				new Vec3fAttribArray("pos", 0, 1, verticesArr, GL40.GL_ARRAY_BUFFER),
				new UIntAttribArray("ind", -1, 1, indicesArr, GL40.GL_ELEMENT_ARRAY_BUFFER),
				new Vec4fAttribArray("col", 1, 1, colorArr, GL40.GL_ARRAY_BUFFER));
	}
	
	public static Mesh loadMesh(String name, String material, String path) {
		String[] lines = FileUtils.readFile(path).split("\n");
		
		List<Vector3f> vertices = new ArrayList<>();
		List<Vector3f> normals = new ArrayList<>();
		List<Vector2f> uvs = new ArrayList<>();
		List<Vector3i> faces = new ArrayList<>();
		
		int li = 0;
		while(li < lines.length) {
			String line = lines[li++];
			String[] tokens = line.split("\\s+");
			
			switch(tokens[0]) {
			case "v":
				vertices.add(new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])
				));
				break;
			case "vt":
				uvs.add(new Vector2f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2])
				));
				break;
			case "vn":
				normals.add(new Vector3f(
						Float.parseFloat(tokens[1]),
						Float.parseFloat(tokens[2]),
						Float.parseFloat(tokens[3])
				));
				break;
			case "f":
				processFace(tokens[1], faces);
				processFace(tokens[2], faces);
				processFace(tokens[3], faces);
				break;
			}
		}
		List<Integer> indices = new ArrayList<>();
		Vector3f[] verticesArr = new Vector3f[vertices.size()];
		int i = 0;
		for(Vector3f pos : vertices) {
			/*verticesArr[i*3+0] = pos.x;
			verticesArr[i*3+1] = pos.y;
			verticesArr[i*3+2] = pos.z;*/
			verticesArr[i] = pos;
			i++;
		}
		
		Vector2f[] uvsArr = new Vector2f[vertices.size()];
		Vector3f[] normalsArr = new Vector3f[vertices.size()];
		
		for(Vector3i face : faces) {
			int pos = face.x;
			int tex = face.y;
			int nor = face.z;
			
			indices.add(pos);
			
			if(tex >= 0) {
				Vector2f v = uvs.get(tex);
				/*uvsArr[pos*2+0] = v.x;
				uvsArr[pos*2+1] = 1-v.y;*/
				uvsArr[pos] = v;
			}
			
			if(nor >= 0) {
				Vector3f v = normals.get(nor);
				/*normalsArr[pos*3+0] = v.x;
				normalsArr[pos*3+1] = v.y;
				normalsArr[pos*3+2] = v.z;*/
				normalsArr[pos] = v;
			}
		}
		
		int[] indicesArr = indices.stream().mapToInt((v) -> v).toArray();
		
		Vec3fAttribArray pos = new Vec3fAttribArray("pos", 0, 1, verticesArr, GL40.GL_ARRAY_BUFFER);
		UIntAttribArray ind = new UIntAttribArray("ind", -1, 1, indicesArr, GL40.GL_ELEMENT_ARRAY_BUFFER);
		Vec3fAttribArray norm = new Vec3fAttribArray("norm", 1, 1, normalsArr, GL40.GL_ARRAY_BUFFER);
		Vec2fAttribArray uv = new Vec2fAttribArray("uv", 2, 1, uvsArr, GL40.GL_ARRAY_BUFFER);
		
		return new Mesh(name, material, pos, ind, norm, uv);
	}
	
	private static void processFace(String token, List<Vector3i> faces) {
		String[] tokens = token.split("/");
		int len = tokens.length;
		int pos = -1, coords = -1, normals = -1;
		pos = Integer.parseInt(tokens[0])-1;
		if(len > 1)
			coords = Integer.parseInt(tokens[1])-1;
		if(len > 2)
			normals = Integer.parseInt(tokens[2])-1;
		faces.add(new Vector3i(
				pos, coords, normals
		));
	}
	
}
