package lu.kbra.gamebox.client.es.engine.graph.material.components;

public class PointLightMaterialComponent extends MaterialComponent {

	private String lightName, lightCountName;
	private int maxLights;

	public PointLightMaterialComponent(String name, String countName, int max) {
		this.lightName = name;
		this.lightCountName = countName;
		this.maxLights = max;
	}

	public String getLightCountName() {
		return lightCountName;
	}

	public String getLightName() {
		return lightName;
	}

	public int getMaxLights() {
		return maxLights;
	}

}
