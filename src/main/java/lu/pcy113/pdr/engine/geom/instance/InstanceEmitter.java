package lu.pcy113.pdr.engine.geom.instance;

import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL40;

import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Mat4fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.ArrayUtils;
import lu.pcy113.pdr.engine.utils.transform.Transform;
import lu.pcy113.pdr.engine.utils.transform.Transform2D;
import lu.pcy113.pdr.engine.utils.transform.Transform3D;
import lu.pcy113.pdr.utils.Logger;

public class InstanceEmitter implements Renderable, Cleanupable, UniqueID {
	
	public static final String NAME = InstanceEmitter.class.getName();
	
	protected final String name;
	
	protected Instance[] particles;
	
	protected Mat4fAttribArray instancesTransforms;
	protected AttribArray[] instancesAttribs;
	protected final int count;
	
	protected Mesh instanceMesh;
	
	public InstanceEmitter(String name, Mesh mesh, int count, Transform<?> baseTransform, AttribArray... attribs) {
		this.name = name;
		this.count = count;
		
		particles = new Instance[count];
		Matrix4f[] transforms = new Matrix4f[count];
		for(int i = 0; i < count; i++) {
			Object[] atts = new Object[attribs.length];
			for(int a = 0; a < attribs.length; a++) {
				atts[a] = attribs[a].get(i);
			}
			particles[i] = new Instance(i, baseTransform.clone(), atts);
			
			if(particles[i].getTransform() instanceof Transform2D) {
				FloatBuffer fb = BufferUtils.createFloatBuffer(16);
				((Transform2D) particles[i].getTransform()).getMatrix().get4x4(fb);
				fb.flip();
				transforms[i] = new Matrix4f(fb);
			}else if(particles[i].getTransform() instanceof Transform3D) {
				transforms[i] = ((Transform3D) particles[i].getTransform()).getMatrix();
			}
		}
		
		this.instancesTransforms = new Mat4fAttribArray("transforms", 3, 1, transforms, GL40.GL_ARRAY_BUFFER, false, 1);
		
		this.instancesAttribs = attribs;
		this.instanceMesh = mesh;
		
		mesh.bind();
		
		mesh.storeAttribArray(instancesTransforms);
		for(AttribArray a : instancesAttribs) {
			if(mesh.getVbo().containsKey(a.getIndex())) {
				Logger.log(Level.WARNING, "Duplicate of index: "+a.getIndex()+" from "+a.getName()+", in Mesh: "+name);
				continue;
			}
			mesh.storeAttribArray(a);
		}
		
		mesh.unbind();
		
		System.err.println(instancesTransforms);
		
		Logger.log(Level.INFO, "ParticleEmitter "+name+": m:("+mesh.getId()+" & "+mesh.getVbo()+"); c:"+count);
	}
	
	/**
	 * <h3>DOES NOT CALL Transform#updateMatrix()</h3>
	 */
	public void update(Consumer<Instance> update) {
		Matrix4f[] transforms = new Matrix4f[count];
		Object[][] atts = new Object[instancesAttribs.length][];
		for(int i = 0; i < count; i++) {
			update.accept(particles[i]);
			
			if(particles[i].getTransform() instanceof Transform2D) {
				FloatBuffer fb = BufferUtils.createFloatBuffer(16);
				((Transform2D) particles[i].getTransform()).getMatrix().get4x4(fb);
				fb.flip();
				transforms[i] = new Matrix4f(fb);
			}else if(particles[i].getTransform() instanceof Transform3D) {
				transforms[i] = ((Transform3D) particles[i].getTransform()).getMatrix();
			}
				
			for(int c = 0; c < instancesAttribs.length; c++) {
				atts[c][i] = particles[i].getBuffers()[c];
			}
		}
		System.out.println("update transforms... "+AttribArray.update(instancesTransforms, transforms));
		for(int c = 0; c < instancesAttribs.length; c++)
			AttribArray.update(instancesAttribs[c], atts[c]);
		GL40.glBindBuffer(GL40.GL_ARRAY_BUFFER, 0);
	}
	
	public void bind() {
		instanceMesh.bind();
	}
	public void unbind() {
		instanceMesh.unbind();
	}
	
	@Override
	public void cleanup() {
		Arrays.stream(instancesAttribs).forEach(AttribArray::cleanup);
		instancesTransforms.cleanup();
		
		instanceMesh.cleanup();
	}
	
	@Override
	public String getId() {return name;}
	public int getParticleCount() {return count;}
	public Mesh getParticleMesh() {return instanceMesh;}
	public AttribArray[] getParticleAttribs() {return instancesAttribs;}
	public AttribArray getParticleTransforms() {return instancesTransforms;}
	
}
