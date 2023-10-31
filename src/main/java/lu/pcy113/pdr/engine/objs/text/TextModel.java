package lu.pcy113.pdr.engine.objs.text;

import org.joml.Vector2f;
import org.joml.Vector3f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.attrib.Vec3fAttribArray;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.text.TextMaterial;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;

public class TextModel implements UniqueID, Renderable {
	
	public static final String NAME = TextModel.class.getName();
	
	public static float TAB_SIZE = 4;
	
	private final String name;
	
	private Vector2f charSize;
	private Transform3D transform;
	private CharSequence text;
	
	private String material;
	
	public TextModel(String name, TextMaterial tMaterial, Transform3D transform, CharSequence text, Vector2f size) {
		this.name = name;
		
		this.transform = transform;
		this.material = tMaterial.getId();
		this.text = text;
		this.charSize = size;
	}
	
	public boolean bindText(CacheManager cache, Vec3fAttribArray vertices, Material material) {
		material.setProperty(TextShader.TXT_LENGTH, text.length());
		Vector3f[] nPos = new Vector3f[vertices.getLength()];
		int currentLine = 0;
		int currentChar = 0;
		for(int j = 0, i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if(c == '\n') {
				currentChar = 0;
				currentLine++;
				continue;
			}else if(c == '\t') {
				currentChar += TAB_SIZE;
				continue;
			}
			else if(c == ' ') {
				currentChar++;
				continue;
			}
			
			material.setProperty(TextShader.TXT_BUFFER+"["+j+"]", (Character) c);
			
			float cx = (currentChar++)*charSize.x*2;
			float cy = (currentLine)*charSize.y;
			
			nPos[j*4+0] = new Vector3f((cx-0.5f)*(charSize.x), (cy+0.5f)*(charSize.y), 0);
			
			nPos[j*4+1] = new Vector3f((cx+0.5f)*(charSize.x), (cy+0.5f)*(charSize.y), 0);
			
			nPos[j*4+2] = new Vector3f((cx+0.5f)*(charSize.x), (cy-0.5f)*(charSize.y), 0);
			
			nPos[j*4+3] = new Vector3f((cx-0.5f)*(charSize.x), (cy-0.5f)*(charSize.y), 0);
			
			System.out.println(" =================== "+i+" >> "+j+": "+c+"("+nPos[j*3]+", "+nPos[j*3+1]+")");
			j++;
		}
		//material.setProperty(TextShader.CHAR_SIZE, charSize);
		
		vertices.bind();
		if(!vertices.update(nPos)) {
			System.out.println("could not update");
			return false;
		}
		vertices.unbind();
		
		System.err.println("Bound ----\n"+material.getProperties());
		return true;
	}
	
	public String getMaterial() {return material;}
	public Transform3D getTransform() {return transform;}
	public void setTransform(Transform3D transform) {this.transform = transform;}
	public CharSequence getText() {return text;}
	public void setText(CharSequence text) {this.text = text;}
	@Override
	public String getId() {return name;}

}
