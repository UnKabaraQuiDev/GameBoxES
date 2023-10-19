package lu.pcy113.pdr.engine.objs.text;

import org.joml.Vector2f;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.attrib.FloatAttribArray;
import lu.pcy113.pdr.engine.graph.material.Material;
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
	
	public TextModel(String name, String tMaterial, Transform3D transform, CharSequence text, Vector2f size) {
		this.name = name;
		
		this.transform = transform;
		this.material = tMaterial;
		this.text = text;
		this.charSize = size;
	}
	
	public void bindText(CacheManager cache, FloatAttribArray array, Material material) {
		material.setProperty(TextShader.TXT_LENGTH, text.length());
		float[] nPos = new float[array.getLength()];
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
			
			nPos[j*3] = (currentChar++)*charSize.x;
			nPos[j*3+1] = (currentLine)*charSize.y;
			nPos[j*3+2] = 0;
			
			System.out.println(" =================== "+i+" >> "+j+"("+nPos[j*3]+", "+nPos[j*3+1]+")");
			j++;
		}
		material.setProperty(TextShader.CHAR_SIZE, charSize);
		
		array.bind();
		array.update(nPos);
		array.unbind();
		
		System.err.println("Bound ----\n"+material.getProperties());
	}
	
	public String getMaterial() {return material;}
	public Transform3D getTransform() {return transform;}
	public void setTransform(Transform3D transform) {this.transform = transform;}
	public CharSequence getText() {return text;}
	public void setText(CharSequence text) {this.text = text;}
	@Override
	public String getId() {return name;}

}
