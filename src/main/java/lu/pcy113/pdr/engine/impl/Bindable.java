package lu.pcy113.pdr.engine.impl;

public interface Bindable<T> {
	
	T bind();
	void unbind();
	
}
