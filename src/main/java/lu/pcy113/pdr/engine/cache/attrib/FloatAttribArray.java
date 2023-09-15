package lu.pcy113.pdr.engine.cache.attrib;

public class FloatAttribArray extends AttribArray {
	
	private float[] data;
	
	public FloatAttribArray(String name, int index, int dataSize, float[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	
	public float[] getData() {return data;}
	
	@Override
	public int getLength() {
		return data.length;
	}
	
}
