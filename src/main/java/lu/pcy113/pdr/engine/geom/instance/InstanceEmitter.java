package lu.pcy113.pdr.engine.geom.instance;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.cache.attrib.AttribArray;
import lu.pcy113.pdr.engine.cache.attrib.Mat4fAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.Renderable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.transform.Transform;

public class InstanceEmitter
		implements
		Renderable,
		Cleanupable,
		UniqueID {

	public static final String NAME = InstanceEmitter.class.getName();

	protected final String name;

	protected Instance[] particles;

	protected Mat4fAttribArray instancesTransforms;
	protected AttribArray[] instancesAttribs;
	protected final int count;

	protected Mesh instanceMesh;

	public InstanceEmitter(String name, Mesh mesh, int count, Transform baseTransform, AttribArray... attribs) {
		this.name = name;
		this.count = count;

		this.particles = new Instance[count];
		Matrix4f[] transforms = new Matrix4f[count];
		for (int i = 0; i < count; i++) {
			Object[] atts = new Object[attribs.length];
			for (int a = 0; a < attribs.length; a++) {
				atts[a] = attribs[a].get(i);
			}
			this.particles[i] = new Instance(i, baseTransform.clone(), atts);
			
			transforms[i] = this.particles[i].getTransform().getMatrix();
		}

		this.instancesTransforms = new Mat4fAttribArray("transforms", 3, 1, transforms, GL15.GL_ARRAY_BUFFER, false, 1);

		this.instancesAttribs = attribs;
		this.instanceMesh = mesh;

		mesh.bind();

		mesh.storeAttribArray(this.instancesTransforms);
		for (AttribArray a : this.instancesAttribs) {
			if (mesh.getVbo().containsKey(a.getIndex())) {
				GlobalLogger.log(Level.WARNING, "Duplicate of index: " + a.getIndex() + " from " + a.getName() + ", in Mesh: " + name);
				continue;
			}
			mesh.storeAttribArray(a);
		}

		mesh.unbind();

		System.err.println(this.instancesTransforms);

		GlobalLogger.log(Level.INFO, "ParticleEmitter " + name + ": m:(" + mesh.getId() + " & " + mesh.getVbo() + "); c:" + count);
	}

	/**
	 * <h3>DOES NOT CALL Transform#updateMatrix()</h3>
	 */
	public void update(Consumer<Instance> update) {
		Matrix4f[] transforms = new Matrix4f[this.count];
		Object[][] atts = new Object[this.instancesAttribs.length][];
		for (int i = 0; i < this.count; i++) {
			update.accept(this.particles[i]);

			/*if (this.particles[i].getTransform() instanceof Transform2D) {
				FloatBuffer fb = BufferUtils.createFloatBuffer(16);
				((Transform2D) this.particles[i].getTransform()).getMatrix().get4x4(fb);
				fb.flip();
				transforms[i] = new Matrix4f(fb);
			} else if (this.particles[i].getTransform() instanceof Transform3D) {*/
				transforms[i] = this.particles[i].getTransform().getMatrix();
			//}

			for (int c = 0; c < this.instancesAttribs.length; c++) {
				atts[c][i] = this.particles[i].getBuffers()[c];
			}
		}
		GlobalLogger.log(Level.INFO, "update transforms... " + AttribArray.update(this.instancesTransforms, transforms));
		for (int c = 0; c < this.instancesAttribs.length; c++) {
			AttribArray.update(this.instancesAttribs[c], atts[c]);
		}
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	public void bind() {
		this.instanceMesh.bind();
	}

	public void unbind() {
		this.instanceMesh.unbind();
	}

	@Override
	public void cleanup() {
		Arrays.stream(this.instancesAttribs).forEach(AttribArray::cleanup);
		this.instancesTransforms.cleanup();

		this.instanceMesh.cleanup();
	}

	@Override
	public String getId() { return this.name; }

	public int getParticleCount() { return this.count; }

	public Mesh getParticleMesh() { return this.instanceMesh; }

	public AttribArray[] getParticleAttribs() { return this.instancesAttribs; }

	public AttribArray getParticleTransforms() { return this.instancesTransforms; }

}
