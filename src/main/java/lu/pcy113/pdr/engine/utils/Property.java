package lu.pcy113.pdr.engine.utils;

import java.util.Objects;

public class Property<T> {
	
	private T value;
	private boolean changed = true;
	
	public Property() {}
	public Property(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		if(Objects.equals(this.value, value))
			return;
		
		this.value = value;
		this.changed = true;
	}
	
	public T readValue() {
		this.changed = false;
		return value;
	}
	
	public void setChanged(boolean changed) {
		this.changed = changed;
	}
	
	public boolean isChanged() {
		return changed;
	}
	
}
