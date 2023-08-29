package lu.pcy113.pdr.engine.impl;

public interface ChildObject<T> {
	
	void attach(T obj);
	T getAttached();
	
}
