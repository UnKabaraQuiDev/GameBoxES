package lu.pcy113.pdr.engine.cache;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.audio.Sound;
import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.exceptions.opengl.ShaderInstantiationException;
import lu.pcy113.pdr.engine.geom.Gizmo;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.geom.utils.ObjLoader;
import lu.pcy113.pdr.engine.graph.composition.Framebuffer;
import lu.pcy113.pdr.engine.graph.composition.RenderLayer;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.graph.render.Renderer;
import lu.pcy113.pdr.engine.graph.shader.RenderShader;
import lu.pcy113.pdr.engine.graph.texture.CubemapTexture;
import lu.pcy113.pdr.engine.graph.texture.SingleTexture;
import lu.pcy113.pdr.engine.graph.texture.Texture;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.objs.lights.PointLight;
import lu.pcy113.pdr.engine.objs.text.TextEmitter;
import lu.pcy113.pdr.engine.scene.Scene;
import lu.pcy113.pdr.engine.utils.consts.TextureFilter;
import lu.pcy113.pdr.engine.utils.consts.TextureType;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class CacheManager implements Cleanupable {

	protected Map<String, Mesh> meshes;
	protected Map<String, Scene> scenes;
	protected Map<String, Renderer<?, ?>> renderers;
	protected Map<String, Material> materials;
	protected Map<String, RenderShader> renderShaders;
	protected Map<String, Texture> textures;
	protected Map<String, PointLight> pointLights;
	protected Map<String, Gizmo> gizmos;
	protected Map<String, RenderLayer> renderLayers;
	protected Map<String, TextEmitter> textEmitters;
	protected Map<String, InstanceEmitter> instanceEmitters;
	protected Map<String, Sound> sounds;
	protected Map<String, Framebuffer> framebuffers;

	public CacheManager() {
		this.meshes = new HashMap<>();
		this.scenes = new HashMap<>();
		this.renderers = new HashMap<>();
		this.materials = new HashMap<>();
		this.renderShaders = new HashMap<>();
		this.textures = new HashMap<>();
		this.pointLights = new HashMap<>();
		this.gizmos = new HashMap<>();
		this.renderLayers = new HashMap<>();
		this.textEmitters = new HashMap<>();
		this.instanceEmitters = new HashMap<>();
		this.sounds = new HashMap<>();
		this.framebuffers = new HashMap<>();
	}

	@Override
	public void cleanup() {
		GlobalLogger.log();

		this.meshes.values().forEach(Mesh::cleanup);
		this.meshes.clear();

		this.scenes.values().forEach(Scene::cleanup);
		this.scenes.clear();

		this.renderers.values().forEach(Renderer::cleanup);
		this.renderers.clear();

		// materials.values().forEach(Material::cleanup);
		this.materials.clear();

		this.renderShaders.values().forEach(RenderShader::cleanup);
		this.renderShaders.clear();

		this.textures.values().forEach(Texture::cleanup);
		this.textures.clear();

		// pointLights.values().forEach(PointLight::cleanup);
		this.pointLights.clear();

		this.gizmos.values().forEach(Gizmo::cleanup);
		this.gizmos.clear();

		this.renderLayers.values().forEach(RenderLayer::cleanup);
		this.renderLayers.clear();

		textEmitters.values().forEach(TextEmitter::cleanup);
		this.textEmitters.clear();

		this.instanceEmitters.values().forEach(InstanceEmitter::cleanup);
		this.instanceEmitters.clear();

		this.sounds.values().forEach(Sound::cleanup);
		this.sounds.clear();

		this.framebuffers.values().forEach(Framebuffer::cleanup);
		this.framebuffers.clear();
	}

	public void cleanupSounds() {
		this.sounds.values().forEach(Sound::cleanup);
		this.sounds.clear();
	}

	/*
	 * ADD
	 */

	public boolean addMesh(Mesh m) {
		if (m == null)
			return false;
		if (this.meshes.containsKey(m.getId()) && !this.meshes.get(m.getId()).equals(m))
			this.meshes.remove(m.getId()).cleanup();
		return this.meshes.putIfAbsent(m.getId(), m) == null;
	}

	/*
	 * private boolean add(Map<String, UniqueID> map, UniqueID m) { if (m == null)
	 * return false;
	 * 
	 * if (map.containsKey(m.getId()) && !map.get(m.getId()).equals(m))
	 * map.remove(m.getId());
	 * 
	 * return map.putIfAbsent(m.getId(), (UniqueID) m) == null; }
	 * 
	 * private boolean addCleanup(Map<String, UniqueID> map, UniqueID m) { if (m ==
	 * null) return false;
	 * 
	 * if (map.containsKey(m.getId()) && !map.get(m.getId()).equals(m))
	 * ((Cleanupable) map.remove(m.getId())).cleanup();
	 * 
	 * return map.putIfAbsent(m.getId(), (UniqueID) m) == null; }
	 */

	public boolean addScene(Scene m) {
		if (m == null)
			return false;
		if (this.scenes.containsKey(m.getId()) && !this.scenes.get(m.getId()).equals(m))
			this.scenes.remove(m.getId()).cleanup();
		return this.scenes.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addRenderer(Renderer<?, ?> m) {
		if (m == null)
			return false;
		if (this.renderers.containsKey(m.getId()) && !this.renderers.get(m.getId()).equals(m))
			this.renderers.remove(m.getId()).cleanup();
		return this.renderers.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addMaterial(Material m) {
		if (m == null)
			return false;
		if (this.materials.containsKey(m.getId()) && !this.materials.get(m.getId()).equals(m))
			this.materials.remove(m.getId());
		return this.materials.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addRenderShader(RenderShader m) {
		if (m == null)
			return false;
		if (this.renderShaders.containsKey(m.getId()) && !this.renderShaders.get(m.getId()).equals(m))
			this.renderShaders.remove(m.getId()).cleanup();
		return this.renderShaders.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addTexture(Texture m) {
		if (m == null)
			return false;
		if (this.textures.containsKey(m.getId()) && !this.textures.get(m.getId()).equals(m))
			this.textures.remove(m.getId()).cleanup();
		return this.textures.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addPointLight(PointLight m) {
		if (m == null)
			return false;
		if (this.pointLights.containsKey(m.getId()) && !this.pointLights.get(m.getId()).equals(m))
			this.pointLights.remove(m.getId());
		return this.pointLights.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addGizmo(Gizmo m) {
		if (m == null)
			return false;
		if (this.gizmos.containsKey(m.getId()) && !this.gizmos.get(m.getId()).equals(m))
			this.gizmos.remove(m.getId()).cleanup();
		return this.gizmos.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addRenderLayer(RenderLayer m) {
		if (m == null)
			return false;
		if (this.renderLayers.containsKey(m.getId()) && !this.renderLayers.get(m.getId()).equals(m))
			this.renderLayers.remove(m.getId()).cleanup();
		return this.renderLayers.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addTextEmitter(TextEmitter m) {
		if (m == null)
			return false;
		if (this.textEmitters.containsKey(m.getId()) && !this.textEmitters.get(m.getId()).equals(m))
			this.textEmitters.remove(m.getId()).cleanup();
		return this.textEmitters.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addInstanceEmitter(InstanceEmitter m) {
		if (m == null)
			return false;
		if (this.instanceEmitters.containsKey(m.getId()) && !this.instanceEmitters.get(m.getId()).equals(m))
			this.instanceEmitters.remove(m.getId()).cleanup();
		return this.instanceEmitters.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addSound(Sound m) {
		if (m == null)
			return false;
		if (this.sounds.containsKey(m.getId()) && !this.sounds.get(m.getId()).equals(m))
			this.sounds.remove(m.getId()).cleanup();
		return this.sounds.putIfAbsent(m.getId(), m) == null;
	}

	public boolean addFramebuffer(Framebuffer m) {
		if (m == null)
			return false;
		if (this.framebuffers.containsKey(m.getId()) && !this.framebuffers.get(m.getId()).equals(m))
			this.framebuffers.remove(m.getId()).cleanup();
		return this.framebuffers.putIfAbsent(m.getId(), m) == null;
	}

	/*
	 * GET
	 */

	public Mesh getMesh(String name) {
		return this.meshes.get(name);
	}

	public Renderer<?, ?> getRenderer(String name) {
		/*
		 * if (name != null && !renderers.containsKey(name))
		 * GlobalLogger.log("No renderer found for: " + name);
		 */
		return this.renderers.get(name);
	}

	public Scene getScene(String name) {
		return this.scenes.get(name);
	}

	public Material getMaterial(String name) {
		return this.materials.get(name);
	}

	public RenderShader getRenderShader(String name) {
		return this.renderShaders.get(name);
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

	public RenderLayer getRenderLayer(String name) {
		return this.renderLayers.get(name);
	}

	public TextEmitter getTextEmitter(String name) {
		return this.textEmitters.get(name);
	}

	public InstanceEmitter getInstanceEmitter(String name) {
		return this.instanceEmitters.get(name);
	}

	public Sound getSound(String name) {
		return this.sounds.get(name);
	}

	public Framebuffer getFramebuffer(String name) {
		return this.framebuffers.get(name);
	}

	/*
	 * GLOBAL S/GETTERS
	 */

	public Map<String, Mesh> getMeshes() {
		return this.meshes;
	}

	public void setMeshes(Map<String, Mesh> meshes) {
		this.meshes = meshes;
	}

	public Map<String, Scene> getScenes() {
		return this.scenes;
	}

	public void setScenes(Map<String, Scene> scenes) {
		this.scenes = scenes;
	}

	public Map<String, Renderer<?, ?>> getRenderers() {
		return this.renderers;
	}

	public void setRenderers(Map<String, Renderer<?, ?>> renderers) {
		this.renderers = renderers;
	}

	public Map<String, Material> getMaterials() {
		return this.materials;
	}

	public void setMaterials(Map<String, Material> materials) {
		this.materials = materials;
	}

	public Map<String, RenderShader> getRenderShaders() {
		return this.renderShaders;
	}

	public void setRenderShaders(Map<String, RenderShader> shaders) {
		this.renderShaders = shaders;
	}

	public Map<String, Texture> getTextures() {
		return this.textures;
	}

	public void setTextures(Map<String, Texture> textures) {
		this.textures = textures;
	}

	public Map<String, Gizmo> getGizmos() {
		return this.gizmos;
	}

	public void setGizmos(Map<String, Gizmo> gizmos) {
		this.gizmos = gizmos;
	}

	public Map<String, RenderLayer> getRenderLayers() {
		return this.renderLayers;
	}

	public void setRenderLayers(Map<String, RenderLayer> renderLayers) {
		this.renderLayers = renderLayers;
	}

	public Map<String, TextEmitter> getTextEmitters() {
		return textEmitters;
	}

	public void setTextEmitters(Map<String, TextEmitter> textEmitters) {
		this.textEmitters = textEmitters;
	}

	public Map<String, InstanceEmitter> getInstanceEmitters() {
		return this.instanceEmitters;
	}

	public void setInstanceEmitters(Map<String, InstanceEmitter> instanceEmitters) {
		this.instanceEmitters = instanceEmitters;
	}

	public Map<String, PointLight> getPointLights() {
		return this.pointLights;
	}

	public void setPointLights(Map<String, PointLight> pointLights) {
		this.pointLights = pointLights;
	}

	public Map<String, Sound> getSounds() {
		return sounds;
	}

	public void setSounds(Map<String, Sound> sounds) {
		this.sounds = sounds;
	}

	public Map<String, Framebuffer> getFramebuffers() {
		return framebuffers;
	}

	public void setFramebuffers(Map<String, Framebuffer> framebuffers) {
		this.framebuffers = framebuffers;
	}

	/*
	 * CONTAIN
	 */

	public boolean hasRenderShader(String name) {
		return renderShaders.containsKey(name);
	}

	public boolean hasMaterial(String name) {
		return materials.containsKey(name);
	}

	public boolean hasMesh(String name) {
		return meshes.containsKey(name);
	}

	public boolean hasFramebuffer(String name) {
		return framebuffers.containsKey(name);
	}

	public boolean hasSound(String name) {
		return sounds.containsKey(name);
	}

	/*
	 * LOADER
	 */

	public <T extends Material> Material loadOrGetMaterial(String name, Class<T> clazz, Object... args) {
		if (materials.containsKey(name)) {
			return materials.get(name);
		} else {
			return loadMaterial(clazz, name, args);
		}
	}

	public <T extends Material> Material loadMaterial(Class<T> clazz, Object... args) {
		try {
			Class[] types = new Class[args.length];
			for (int i = 0; i < args.length; i++) {
				types[i] = args[i].getClass();
			}

			Material mat = clazz.getConstructor(types).newInstance(args);

			addMaterial(mat);
			addRenderShader(mat.getRenderShader());

			return mat;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ShaderInstantiationException(e);
		}
	}

	public Texture loadSingleTexture(String string, String path) {
		Texture texture = new SingleTexture(string, path);
		texture.setup();
		addTexture(texture);
		return texture;
	}

	public Texture loadSingleTexture(String string, String path, TextureFilter filter, TextureType type) {
		Texture texture = new SingleTexture(string, path);
		texture.setFilters(filter);
		texture.setTextureType(type);
		texture.setup();
		addTexture(texture);
		return texture;
	}

	public InstanceEmitter loadInstanceEmitter(String name, Mesh mesh, int count, Transform baseTransform, AttribArray... attribArrays) {
		InstanceEmitter instanceEmitter = new InstanceEmitter(name, mesh, count, baseTransform, attribArrays);
		addInstanceEmitter(instanceEmitter);
		return instanceEmitter;
	}

	public Mesh loadMesh(String name, Material material, String path) {
		Mesh mesh = null;

		if (path.endsWith("obj"))
			mesh = ObjLoader.loadMesh(name, material, path);

		if (mesh == null)
			throw new RuntimeException("Could not load mesh.");

		addMesh(mesh);
		return mesh;
	}

	public CubemapTexture loadCubemapTexture(String name, String path) {
		CubemapTexture txt = new CubemapTexture(name, path);
		txt.setup();
		addTexture(txt);
		return txt;
	}

	public Framebuffer loadFramebuffer(String name) {
		Framebuffer fb = new Framebuffer(name);
		addFramebuffer(fb);
		return fb;
	}

	public Sound loadSound(String name, String file) {
		return loadSound(name, file, true);
	}

	public Sound loadSound(String name, String file, boolean stereo) {
		if (hasSound(name))
			return getSound(name);
		Sound sound = new Sound(name, file, stereo);
		addSound(sound);
		return sound;
	}

	/*
	 * UTILS
	 */

	public void dump(PrintStream out) {
		out.println("== DUMP:" + this.getClass().getName() + ":start ==");
		out.println(Mesh.class.getName() + ": " + this.meshes.size() + ": " + this.meshes);
		out.println(Scene.class.getName() + ": " + this.scenes.size() + ": " + this.scenes);
		out.println(Renderer.class.getName() + ": " + this.renderers.size() + ": " + this.renderers);
		out.println(Material.class.getName() + ": " + this.materials.size() + ": " + this.materials);
		out.println(RenderShader.class.getName() + ": " + this.renderShaders.size() + ": " + this.renderShaders);
		out.println(Texture.class.getName() + ": " + this.textures.size() + ": " + this.textures);
		out.println(Gizmo.class.getName() + ": " + this.gizmos.size() + ": " + this.gizmos);
		out.println(RenderLayer.class.getName() + ": " + this.renderLayers.size() + ": " + this.renderLayers);
		out.println(TextEmitter.class.getName() + ": " + this.textEmitters.size() + ": " + this.textEmitters);
		out.println(PointLight.class.getName() + ": " + this.pointLights.size() + ": " + this.pointLights);
		out.println("== DUMP:" + this.getClass().getName() + ":end ==");
	}

}
