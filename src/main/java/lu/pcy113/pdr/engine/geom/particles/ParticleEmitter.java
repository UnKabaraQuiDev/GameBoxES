package lu.pcy113.pdr.engine.geom.particles;

import java.util.Arrays;
import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.attrib.Matrix4fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.graph.material.Material;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class ParticleEmitter implements Renderable, Cleanupable, UniqueID {
	
	public static final String NAME = ParticleEmitter.class.getName();
	
	private final String name;
	private final int count;
	private Particle[] particles;
	private Matrix4fAttribArray matrices;
	private String mesh, material;
	
	public ParticleEmitter(String name, int count, Mesh mesh, Material mat, Transform baseTransform) {
		this.name = name;
		this.count = count;
		this.mesh = mesh.getId();
		this.material = mat.getId();
		
		this.particles = new Particle[count];
		
		Matrix4f[] mats = new Matrix4f[count];
		for(int i = 0; i < count; i++) {
			this.particles[i] = new Particle(baseTransform, i);
			mats[i] = particles[i].getTransform().getMatrix();
		}
		this.matrices = new Matrix4fAttribArray("mat4", 3, 1, mats, GL40.GL_ARRAY_BUFFER, false);
		this.matrices.bind();
		this.matrices.init();
		this.matrices.enable();
		this.matrices.unbind();
		GL40.glVertexAttribDivisor(this.matrices.getIndex(), 1);
	}
	
	@Override
	public void cleanup() {
		matrices.cleanup();
	}
	
	public void update(Consumer<Particle> forEach) {
		Arrays.stream(particles).forEach(forEach);
		matrices.update(Arrays.stream(particles).parallel().map(p -> p.getTransform().getMatrix()).toArray(Matrix4f[]::new));
	}
	
	@Override
	public String getId() {
		return name;
	}
	public Particle[] getParticles() {return particles;}
	public void setParticles(Particle[] particles) {this.particles = particles;}
	public int getParticleCount() {return count;}
	public Matrix4fAttribArray getMatrices() {return matrices;}	
	public String getMesh() {return mesh;}
	public String getMaterial() {return material;}
	public void setMaterial(String material) {this.material = material;}
	public void setMesh(String mesh) {this.mesh = mesh;}
	
}
