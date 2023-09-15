package lu.pcy113.pdr.engine.cache.attrib;

public abstract class AttribArray {
	
	protected String name;
	protected int index;
	protected int dataSize;
	
	public AttribArray(String name, int index, int dataSize) {
		this.name = name;
		this.index = index;
		this.dataSize = dataSize;
	}
	
	public String getName() {return name;}
	public int getIndex() {return index;}
	public int getDataSize() {return dataSize;}
	
	public int getDataCount() {
		return getLength()/getDataSize();
	}
	public abstract int getLength();
	
}
