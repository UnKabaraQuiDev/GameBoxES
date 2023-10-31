package lu.pcy113.pdr.engine.cache;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.audio.Sound;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.composition.RenderLayer;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.render.Renderer;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.InstanceEmitterModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.PointLight;
import lu.pcy113.pdr.engine.objs.text.TextModel;
import lu.pcy113.pdr.engine.scene.Scene;

public class CacheManager implements Cleanupable {
	
	protected Map<String, Mesh> meshes;
	protected Map<String, Scene> scenes;
	protected Map<String, Renderer<?, ?>> renderers;
	protected Map<String, Material> materials;
	protected Map<String, Shader> shaders;
	protected Map<String, Model> models;
	protected Map<String, Texture> textures;
	protected Map<String, PointLight> pointLights;
	protected Map<String, Gizmo> gizmos;
	protected Map<String, GizmoModel> gizmoModels;
	protected Map<String, RenderLayer> renderLayers;
	protected Map<String, TextModel> textModels;
	protected Map<String, InstanceEmitter> instanceEmitters;
	protected Map<String, InstanceEmitterModel> instanceEmitterModels;
	protected Map<String, Sound> sounds;
	
	public CacheManager() {
		this.meshes = new HashMap<>();
		this.scenes = new HashMap<>();
		this.renderers = new HashMap<>();
		this.materials = new HashMap<>();
		this.shaders = new HashMap<>();
		this.models = new HashMap<>();
		this.textures = new HashMap<>();
		this.pointLights = new HashMap<>();
		this.gizmos = new HashMap<>();
		this.gizmoModels = new HashMap<>();
		this.renderLayers = new HashMap<>();
		this.textModels = new HashMap<>();
		this.instanceEmitters = new HashMap<>();
		this.instanceEmitterModels = new HashMap<>();
		this.sounds = new HashMap<>();
	}
	
	@Override
	public void cleanup() {
		meshes.values().forEach(Mesh::cleanup);
		meshes.clear();
		
		scenes.values().forEach(Scene::cleanup);
		scenes.clear();
		
		renderers.values().forEach(Renderer::cleanup);
		renderers.clear();
		
		//materials.values().forEach(Material::cleanup);
		materials.clear();
		
		shaders.values().forEach(Shader::cleanup);
		shaders.clear();
		
		//models.values().forEach(Model::cleanup);
		models.clear();
		
		textures.values().forEach(Texture::cleanup);
		textures.clear();
		
		//pointLights.values().forEach(PointLight::cleanup);
		pointLights.clear();
		
		gizmos.values().forEach(Gizmo::cleanup);
		gizmos.clear();
		
		//gizmoModels.values().forEach(GizmoModel::cleanup);
		gizmoModels.clear();
		
		renderLayers.values().forEach(RenderLayer::cleanup);
		renderLayers.clear();
		
		//textModels.values().forEach(TextModel::cleanup);
		textModels.clear();
		
		instanceEmitters.values().forEach(InstanceEmitter::cleanup);
		instanceEmitters.clear();
		
		//instanceEmitterModels.values().forEach(ParticleEmitterModel::cleanup);
		instanceEmitterModels.clear();
	}
	
	public boolean addMesh(Mesh m) {
		if(meshes.containsKey(m.getId())) meshes.remove(m.getId()).cleanup();
		return this.meshes.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addScene(Scene m) {
		if(scenes.containsKey(m.getId())) scenes.remove(m.getId()).cleanup();
		return this.scenes.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addRenderer(Renderer<?, ?> m) {
		if(renderers.containsKey(m.getId())) renderers.remove(m.getId()).cleanup();
		return this.renderers.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addMaterial(Material m) {
		if(materials.containsKey(m.getId())) materials.remove(m.getId());
		return this.materials.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addShader(Shader m) {
		if(shaders.containsKey(m.getId())) shaders.remove(m.getId()).cleanup();
		return this.shaders.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addModel(Model m) {
		if(models.containsKey(m.getId())) models.remove(m.getId());
		return this.models.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addTexture(Texture m) {
		if(textures.containsKey(m.getId())) textures.remove(m.getId()).cleanup();
		return this.textures.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addPointLight(PointLight m) {
		if(pointLights.containsKey(m.getId())) pointLights.remove(m.getId());
		return this.pointLights.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addGizmo(Gizmo m) {
		if(gizmos.containsKey(m.getId())) gizmos.remove(m.getId()).cleanup();
		return this.gizmos.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addGizmoModel(GizmoModel m) {
		if(gizmoModels.containsKey(m.getId())) gizmoModels.remove(m.getId());
		return this.gizmoModels.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addRenderLayer(RenderLayer m) {
		if(renderLayers.containsKey(m.getId())) renderLayers.remove(m.getId()).cleanup();
		return this.renderLayers.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addTextModel(TextModel m) {
		return this.textModels.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addInstanceEmitter(InstanceEmitter m) {
		return this.instanceEmitters.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addInstanceEmitterModel(InstanceEmitterModel m) {
		return this.instanceEmitterModels.putIfAbsent(m.getId(), m) == null;
	}
	
	public Mesh getMesh(String name) {
		return meshes.get(name);
	}
	public Renderer<?, ?> getRenderer(String name) {
		return this.renderers.get(name);
	}
	public Scene getScene(String name) {
		return scenes.get(name);
	}
	public Material getMaterial(String name) {
		return materials.get(name);
	}
	public Shader getShader(String name) {
		return shaders.get(name);
	}
	public Model getModel(String name) {
		return models.get(name);
	}
	public Texture getTexture(String name) {
		return textures.get(name);
	}
	public PointLight getPointLight(String name) {
		return pointLights.get(name);
	}
	public Gizmo getGizmo(String name) {
		return gizmos.get(name);
	}
	public GizmoModel getGizmoModel(String name) {
		return gizmoModels.get(name);
	}
	public RenderLayer getRenderLayer(String name) {
		return renderLayers.get(name);
	}
	public TextModel getTextModel(String name) {
		return textModels.get(name);
	}
	public InstanceEmitter getInstanceEmitter(String name) {
		return instanceEmitters.get(name);
	}
	public InstanceEmitterModel getInstanceEmitterModel(String name) {
		return instanceEmitterModels.get(name);
	}
	
	public Map<String, Mesh> getMeshes() {return meshes;}
	public void setMeshes(Map<String, Mesh> meshes) {this.meshes = meshes;}
	public Map<String, Scene> getScenes() {return scenes;}
	public void setScenes(Map<String, Scene> scenes) {this.scenes = scenes;}
	public Map<String, Renderer<?, ?>> getRenderers() {return renderers;}
	public void setRenderers(Map<String, Renderer<?, ?>> renderers) {this.renderers = renderers;}
	public Map<String, Material> getMaterials() {return materials;}
	public void setMaterials(Map<String, Material> materials) {this.materials = materials;}
	public Map<String, Shader> getShaders() {return shaders;}
	public void setShaders(Map<String, Shader> shaders) {this.shaders = shaders;}
	public Map<String, Texture> getTextures() {return textures;}
	public void setTextures(Map<String, Texture> textures) {this.textures = textures;}
	public Map<String, Gizmo> getGizmos() {return gizmos;}
	public void setGizmos(Map<String, Gizmo> gizmos) {this.gizmos = gizmos;}
	public Map<String, GizmoModel> getGizmoModels() {return gizmoModels;}
	public void setGizmoModels(Map<String, GizmoModel> gizmoModels) {this.gizmoModels = gizmoModels;}
	public Map<String, RenderLayer> getRenderLayers() {return renderLayers;}
	public void setRenderLayers(Map<String, RenderLayer> renderLayers) {this.renderLayers = renderLayers;}
	public Map<String, TextModel> getTextModels() {return textModels;}
	public void setTextModels(Map<String, TextModel> textModels) {this.textModels = textModels;}
	public Map<String, InstanceEmitterModel> getInstanceEmitterModels() {return instanceEmitterModels;}
	public void setInstanceEmitterModels(Map<String, InstanceEmitterModel> instanceEmitterModels) {this.instanceEmitterModels = instanceEmitterModels;}
	public Map<String, InstanceEmitter> getInstanceEmitters() {return instanceEmitters;}
	public void setInstanceEmitters(Map<String, InstanceEmitter> instanceEmitters) {this.instanceEmitters = instanceEmitters;}
	public Map<String, Model> getModels() {return models;}
	public void setModels(Map<String, Model> models) {this.models = models;}
	public Map<String, PointLight> getPointLights() {return pointLights;}
	public void setPointLights(Map<String, PointLight> pointLights) {this.pointLights = pointLights;}
	
}
