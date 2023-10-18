package lu.pcy113.pdr.engine.objs.entity.components;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.objs.PointLight;
import lu.pcy113.pdr.engine.objs.entity.Component;

public class PointLightComponent extends Component {
	
	private String pointLightId;
	
	public PointLightComponent(PointLight pointLight) {
		this.pointLightId = pointLight.getId();
	}
	public PointLightComponent(String pointLightId) {
		this.pointLightId = pointLightId;
	}
	
	public void update(float dUpdate) {}
	public void render(float dRender) {}
	
	public String getPointLightId() {return pointLightId;}
	public void setPointLightId(String pointLightId) {this.pointLightId = pointLightId;}
	
	public PointLight getPointLight(CacheManager cache) {return cache.getPointLight(pointLightId);}
	public void setPointLight(PointLight pointLight) {this.pointLightId = pointLight.getId();}
	
}
