package lu.pcy113.pdr.engine.cache;

import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.material.Shader;
import lu.pcy113.pdr.engine.graph.render.Renderer;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.objs.GizmoModel;
import lu.pcy113.pdr.engine.objs.Model;
import lu.pcy113.pdr.engine.objs.PointLight;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.scene.Scene3D;

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
	}
	
	public boolean addMesh(Mesh mesh) {
		return this.meshes.putIfAbsent(mesh.getId(), mesh) == null;
	}
	public boolean addScene(Scene3D scene) {
		return this.scenes.putIfAbsent(scene.getId(), scene) == null;
	}
	public boolean addRenderer(Renderer<?, ?> renderer) {
		return this.renderers.putIfAbsent(renderer.getId(), renderer) == null;
	}
	public boolean addMaterial(Material material) {
		return this.materials.putIfAbsent(material.getId(), material) == null;
	}
	public boolean addShader(Shader shader) {
		return this.shaders.putIfAbsent(shader.getId(), shader) == null;
	}
	public boolean addModel(Model model) {
		return this.models.putIfAbsent(model.getId(), model) == null;
	}
	public boolean addTexture(Texture texture) {
		return this.textures.putIfAbsent(texture.getId(), texture) == null;
	}
	public boolean addPointLight(PointLight pointLight) {
		return this.pointLights.putIfAbsent(pointLight.getId(), pointLight) == null;
	}
	public boolean addGizmo(Gizmo gizmo) {
		return this.gizmos.putIfAbsent(gizmo.getId(), gizmo) == null;
	}
	public boolean addGizmoModel(GizmoModel gizmoModel) {
		return this.gizmoModels.putIfAbsent(gizmoModel.getId(), gizmoModel) == null;
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
	
}
