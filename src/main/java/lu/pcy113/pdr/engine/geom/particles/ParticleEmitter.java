package lu.pcy113.pdr.engine.geom.particles;

import java.util.Arrays;
import java.util.function.Consumer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.attrib.Matrix4fAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Vec4fAttribArray;
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
	private Vec4fAttribArray[] matrices = new Vec4fAttribArray[4];
	private String mesh, material;
	
	public ParticleEmitter(String name, int count, Mesh mesh, Material mat, Transform baseTransform) {
		this.name = name;
		this.count = count;
		this.mesh = mesh.getId();
		this.material = mat.getId();
		
		this.particles = new Particle[count];
		
		Matrix4f[] mats = new Matrix4f[count];
		Vector4f[] vecs = new Vector4f[count*4];
		for(int i = 0; i < count; i++) {
			this.particles[i] = new Particle(baseTransform, i);
			mats[i] = particles[i].getTransform().getMatrix();
			for(int c = 0; c < 4; c++) {
				int index = c*count+i;
				vecs[index] = new Vector4f();
				particles[i].getTransform().updateMatrix().getColumn(c, vecs[index]);
				System.out.println("t: "+index+" "+vecs[index]);
			}
		}
		for(int i = 0; i < 4; i++) {
			Vector4f[] pa = new Vector4f[count];
			System.out.println((i*count)+" "+pa.length+" / "+vecs.length+" "+((i*count)+count-1));
			System.arraycopy(vecs, i*count, pa, 0, count);
			System.out.println(pa.length+" "+Arrays.toString(pa));
			this.matrices[i] = new Vec4fAttribArray("transform", 3+i, 1, pa, GL40.GL_ARRAY_BUFFER, false);
			/*this.matrices[i].bind();
			this.matrices[i].init();
			this.matrices[i].enable();
			this.matrices[i].unbind();*/
		}
		System.out.println("arrays: "+Arrays.toString(matrices));
	}
	
	@Override
	public void cleanup() {
		Arrays.stream(matrices).forEach(Vec4fAttribArray::cleanup);
	}
	
	public void update(Consumer<Particle> forEach) {
		Arrays.stream(particles).forEach(forEach);
		//matrices.update(Arrays.stream(particles).parallel().map(p -> p.getTransform().getMatrix()).toArray(Matrix4f[]::new));
	}
	
	@Override
	public String getId() {
		return name;
	}
	public Particle[] getParticles() {return particles;}
	public void setParticles(Particle[] particles) {this.particles = particles;}
	public int getParticleCount() {return count;}
	public Vec4fAttribArray[] getMatrices() {return matrices;}	
	public String getMesh() {return mesh;}
	public String getMaterial() {return material;}
	public void setMaterial(String material) {this.material = material;}
	public void setMesh(String mesh) {this.mesh = mesh;}
	
}
