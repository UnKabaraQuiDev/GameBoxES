package lu.pcy113.pdr.engine.graph.material.components;

import lu.pcy113.pdr.engine.graph.material.Material;

public class MaterialComponent {

	private Material parent;

	public boolean attach(Material parent) {
		if (this.parent != null)
			return false;
		this.parent = parent;
		return true;
	}

	public Material getParent() {
		return parent;
	}

}
