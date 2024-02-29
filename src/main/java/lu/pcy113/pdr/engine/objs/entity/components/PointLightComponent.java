package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.objs.lights.PointLight;

public class PointLightComponent extends LightComponent {

	private String pointLightId;

	public PointLightComponent(PointLight pointLight) {
		this.pointLightId = pointLight.getId();
	}

	public PointLightComponent(String pointLightId) {
		this.pointLightId = pointLightId;
	}

	public String getPointLightId() {
		return this.pointLightId;
	}

	public void setPointLightId(String pointLightId) {
		this.pointLightId = pointLightId;
	}

	public PointLight getPointLight(CacheManager cache) {
		return cache.getPointLight(
				this.pointLightId);
	}

	public void setPointLight(PointLight pointLight) {
		this.pointLightId = pointLight.getId();
	}

}
