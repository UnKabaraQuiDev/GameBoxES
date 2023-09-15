package lu.pcy113.pdr.engine.cache.attrib;

public class IntAttribArray extends AttribArray {
	
	private int[] data;
	
	public IntAttribArray(String name, int index, int dataSize, int[] data) {
		super(name, index, dataSize);
		this.data = data;
	}
	
	public int[] getData() {return data;}
	
	@Override
	public int getLength() {
		return data.length;
	}
	
}
