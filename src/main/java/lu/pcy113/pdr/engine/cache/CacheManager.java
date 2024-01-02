package lu.pcy113.pdr.engine.cache;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.audio.Sound;
import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.exceptions.ShaderInstantiationException;
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
import lu.pcy113.pdr.engine.objs.UIModel;
import lu.pcy113.pdr.engine.objs.text.TextModel;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class CacheManager
		implements
		Cleanupable {

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
	protected Map<String, UIModel> uiModels;

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
		this.uiModels = new HashMap<>();
	}

	@Override
	public void cleanup() {
		this.meshes.values().forEach(Mesh::cleanup);
		this.meshes.clear();

		this.scenes.values().forEach(Scene::cleanup);
		this.scenes.clear();

		this.renderers.values().forEach(Renderer::cleanup);
		this.renderers.clear();

		// materials.values().forEach(Material::cleanup);
		this.materials.clear();

		this.shaders.values().forEach(Shader::cleanup);
		this.shaders.clear();

		// models.values().forEach(Model::cleanup);
		this.models.clear();

		this.textures.values().forEach(Texture::cleanup);
		this.textures.clear();

		// pointLights.values().forEach(PointLight::cleanup);
		this.pointLights.clear();

		this.gizmos.values().forEach(Gizmo::cleanup);
		this.gizmos.clear();

		// gizmoModels.values().forEach(GizmoModel::cleanup);
		this.gizmoModels.clear();

		this.renderLayers.values().forEach(RenderLayer::cleanup);
		this.renderLayers.clear();

		// textModels.values().forEach(TextModel::cleanup);
		this.textModels.clear();

		this.instanceEmitters.values().forEach(InstanceEmitter::cleanup);
		this.instanceEmitters.clear();

		// instanceEmitterModels.values().forEach(ParticleEmitterModel::cleanup);
		this.instanceEmitterModels.clear();
		
		this.sounds.values().forEach(Sound::cleanup);
		this.sounds.clear();
		
		//this.uiModels.values().forEach(UIModel::cleanup);
		this.uiModels.clear();
	}

	public boolean addMesh(Mesh m) {
		if (this.meshes.containsKey(m.getId())) this.meshes.remove(m.getId()).cleanup();
		return this.meshes.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addScene(Scene m) {
		if (this.scenes.containsKey(m.getId())) this.scenes.remove(m.getId()).cleanup();
		return this.scenes.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addRenderer(Renderer<?, ?> m) {
		if (this.renderers.containsKey(m.getId())) this.renderers.remove(m.getId()).cleanup();
		return this.renderers.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addMaterial(Material m) {
		if (this.materials.containsKey(m.getId())) this.materials.remove(m.getId());
		return this.materials.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addShader(Shader m) {
		if (this.shaders.containsKey(m.getId())) this.shaders.remove(m.getId()).cleanup();
		return this.shaders.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addModel(Model m) {
		if (this.models.containsKey(m.getId())) this.models.remove(m.getId());
		return this.models.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addTexture(Texture m) {
		if (this.textures.containsKey(m.getId())) this.textures.remove(m.getId()).cleanup();
		return this.textures.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addPointLight(PointLight m) {
		if (this.pointLights.containsKey(m.getId())) this.pointLights.remove(m.getId());
		return this.pointLights.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addGizmo(Gizmo m) {
		if (this.gizmos.containsKey(m.getId())) this.gizmos.remove(m.getId()).cleanup();
		return this.gizmos.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addGizmoModel(GizmoModel m) {
		if (this.gizmoModels.containsKey(m.getId())) this.gizmoModels.remove(m.getId());
		return this.gizmoModels.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addRenderLayer(RenderLayer m) {
		if (this.renderLayers.containsKey(m.getId())) this.renderLayers.remove(m.getId()).cleanup();
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
	public boolean addSound(Sound m) {
		return this.sounds.putIfAbsent(m.getId(), m) == null;
	}
	public boolean addUIModel(UIModel m) {
		return this.uiModels.putIfAbsent(m.getId(), m) == null;
	}

	public Mesh getMesh(String name) {
		return this.meshes.get(name);
	}

	public Renderer<?, ?> getRenderer(String name) {
		/*if (name != null && !renderers.containsKey(name))
			GlobalLogger.log("No renderer found for: " + name);*/
		return this.renderers.get(name);
	}

	public Scene getScene(String name) {
		return this.scenes.get(name);
	}

	public Material getMaterial(String name) {
		return this.materials.get(name);
	}

	public Shader getShader(String name) {
		return this.shaders.get(name);
	}

	public Model getModel(String name) {
		return this.models.get(name);
	}

	public Texture getTexture(String name) {
		return this.textures.get(name);
	}

	public PointLight getPointLight(String name) {
		return this.pointLights.get(name);
	}

	public Gizmo getGizmo(String name) {
		return this.gizmos.get(name);
	}

	public GizmoModel getGizmoModel(String name) {
		return this.gizmoModels.get(name);
	}

	public RenderLayer getRenderLayer(String name) {
		return this.renderLayers.get(name);
	}

	public TextModel getTextModel(String name) {
		return this.textModels.get(name);
	}

	public InstanceEmitter getInstanceEmitter(String name) {
		return this.instanceEmitters.get(name);
	}

	public InstanceEmitterModel getInstanceEmitterModel(String name) {
		return this.instanceEmitterModels.get(name);
	}
	
	public Sound getSound(String name) {
		return this.sounds.get(name);
	}
	
	public UIModel getUIModel(String name) {
		return this.uiModels.get(name);
	}

	public Map<String, Mesh> getMeshes() { return this.meshes; }
	public void setMeshes(Map<String, Mesh> meshes) { this.meshes = meshes; }
	public Map<String, Scene> getScenes() { return this.scenes; }
	public void setScenes(Map<String, Scene> scenes) { this.scenes = scenes; }
	public Map<String, Renderer<?, ?>> getRenderers() { return this.renderers; }
	public void setRenderers(Map<String, Renderer<?, ?>> renderers) { this.renderers = renderers; }
	public Map<String, Material> getMaterials() { return this.materials; }
	public void setMaterials(Map<String, Material> materials) { this.materials = materials; }
	public Map<String, Shader> getShaders() { return this.shaders; }
	public void setShaders(Map<String, Shader> shaders) { this.shaders = shaders; }
	public Map<String, Texture> getTextures() { return this.textures; }
	public void setTextures(Map<String, Texture> textures) { this.textures = textures; }
	public Map<String, Gizmo> getGizmos() { return this.gizmos; }
	public void setGizmos(Map<String, Gizmo> gizmos) { this.gizmos = gizmos; }
	public Map<String, GizmoModel> getGizmoModels() { return this.gizmoModels; }
	public void setGizmoModels(Map<String, GizmoModel> gizmoModels) { this.gizmoModels = gizmoModels; }
	public Map<String, RenderLayer> getRenderLayers() { return this.renderLayers; }
	public void setRenderLayers(Map<String, RenderLayer> renderLayers) { this.renderLayers = renderLayers; }
	public Map<String, TextModel> getTextModels() { return this.textModels; }
	public void setTextModels(Map<String, TextModel> textModels) { this.textModels = textModels; }
	public Map<String, InstanceEmitterModel> getInstanceEmitterModels() { return this.instanceEmitterModels; }
	public void setInstanceEmitterModels(Map<String, InstanceEmitterModel> instanceEmitterModels) {this.instanceEmitterModels = instanceEmitterModels;}
	public Map<String, InstanceEmitter> getInstanceEmitters() { return this.instanceEmitters; }
	public void setInstanceEmitters(Map<String, InstanceEmitter> instanceEmitters) { this.instanceEmitters = instanceEmitters; }
	public Map<String, Model> getModels() { return this.models; }
	public void setModels(Map<String, Model> models) { this.models = models; }
	public Map<String, PointLight> getPointLights() { return this.pointLights; }
	public void setPointLights(Map<String, PointLight> pointLights) { this.pointLights = pointLights; }

	public <T extends Material> Material loadMaterial(Class<T> clazz, Object... args) {
		try {
			Class[] types = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				types[i] = args[i].getClass();
			}
			
			Material mat = clazz.getConstructor(types).newInstance(args);
			
			addMaterial(mat);
			addShader(mat.getShader());
			
			return mat;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new ShaderInstantiationException(e);
		}
	}

	public void dump(PrintStream out) {
		out.println("== DUMP:"+this.getClass().getName()+":start ==");
		out.println(Mesh.class.getName()+": "+this.meshes.size()+": "+this.meshes);
		out.println(Scene.class.getName()+": "+this.scenes.size()+": "+this.scenes);
		out.println(Renderer.class.getName()+": "+this.renderers.size()+": "+this.renderers);
		out.println(Material.class.getName()+": "+this.materials.size()+": "+this.materials);
		out.println(Shader.class.getName()+": "+this.shaders.size()+": "+this.shaders);
		out.println(Texture.class.getName()+": "+this.textures.size()+": "+this.textures);
		out.println(Gizmo.class.getName()+": "+this.gizmos.size()+": "+this.gizmos);
		out.println(GizmoModel.class.getName()+": "+this.gizmoModels.size()+": "+this.gizmoModels);
		out.println(RenderLayer.class.getName()+": "+this.renderLayers.size()+": "+this.renderLayers);
		out.println(TextModel.class.getName()+": "+this.textModels.size()+": "+this.textModels);
		out.println(InstanceEmitterModel.class.getName()+": "+this.instanceEmitterModels.size()+": "+this.instanceEmitterModels);
		out.println(InstanceEmitter.class.getName()+": "+this.instanceEmitters.size()+": "+this.instanceEmitters);
		out.println(Model.class.getName()+": "+this.models.size()+": "+this.models);
		out.println(PointLight.class.getName()+": "+this.pointLights.size()+": "+this.pointLights);
		out.println("== DUMP:"+this.getClass().getName()+":end ==");
	}

	public Texture loadTexture(String string, String path) {
		Texture texture = new Texture(string, path);
		addTexture(texture);
		return texture;
	}

	public InstanceEmitter loadInstanceEmitter(String name, Mesh mesh, int count, Transform baseTransform, AttribArray... attribArrays) {
		InstanceEmitter instanceEmitter = new InstanceEmitter(name, mesh, count, baseTransform, attribArrays);
		addInstanceEmitter(instanceEmitter);
		return instanceEmitter;
	}

}
