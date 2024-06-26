package lu.kbra.gamebox.client.es.engine.geom.instance;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.logging.Level;

import org.joml.Matrix4f;

import lu.kbra.gamebox.client.es.engine.cache.attrib.AttribArray;
import lu.kbra.gamebox.client.es.engine.cache.attrib.Mat4fAttribArray;
import lu.kbra.gamebox.client.es.engine.geom.Mesh;
import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.impl.Renderable;
import lu.kbra.gamebox.client.es.engine.impl.UniqueID;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.kbra.gamebox.client.es.engine.utils.transform.Transform;
import lu.pcy113.pclib.logger.GlobalLogger;

public class InstanceEmitter implements Renderable, Cleanupable, UniqueID {

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

		this.instancesTransforms = new Mat4fAttribArray("transforms", 3, 1, transforms, GL_W.GL_ARRAY_BUFFER, false, 1);

		this.instancesAttribs = attribs;
		this.instanceMesh = mesh;

		mesh.bind();

		mesh.addAttribArray(this.instancesTransforms);
		for (AttribArray a : this.instancesAttribs) {
			if (mesh.getVbo().containsKey(a.getIndex())) {
				GlobalLogger.log(Level.WARNING, "Duplicate of index: " + a.getIndex() + " from " + a.getName() + ", in Mesh: " + name);
				continue;
			}
			mesh.addAttribArray(a);
		}

		mesh.unbind();

		// System.err.println(this.instancesTransforms);

		GlobalLogger.log(Level.INFO, "ParticleEmitter " + name + ": mesh:(" + mesh.getId() + " & " + mesh.getVbo() + "); count:" + count + "; attribs: " + Arrays.toString(attribs));
	}

	/**
	 * <h3>DOES NOT CALL Transform#updateMatrix()</h3>
	 */
	public void update(Consumer<Instance> update) {
		Matrix4f[] transforms = new Matrix4f[this.count];
		Object[][] atts = new Object[this.instancesAttribs.length][this.count];

		for (int i = 0; i < this.count; i++) {
			update.accept(this.particles[i]);

			transforms[i] = this.particles[i].getTransform().getMatrix();

			for (int c = 0; c < this.instancesAttribs.length; c++) {
				atts[c][i] = this.particles[i].getBuffers()[c];
			}
		}

		if (!AttribArray.update(this.instancesTransforms, transforms)) {
			GlobalLogger.log(Level.WARNING, "Could not update transforms");
		}

		for (int c = 0; c < this.instancesAttribs.length; c++) {
			if (!AttribArray.update(this.instancesAttribs[c], atts[c])) {
				GlobalLogger.log(Level.WARNING, "Failed to update attrib array: " + this.instancesAttribs[c].getName());
			}
		}
		GL_W.glBindBuffer(GL_W.GL_ARRAY_BUFFER, 0);
	}

	public void updateDirect(Matrix4f[] transforms, Object[][] atts) {
		if (transforms.length != this.count || atts.length != this.instancesAttribs.length)
			throw new IllegalArgumentException();
		
		// update this.particles to the new values ?
		
		if (!AttribArray.update(this.instancesTransforms, transforms))
			GlobalLogger.log(Level.WARNING, "Could not update transforms");

		
		for (int c = 0; c < this.instancesAttribs.length; c++) {
			if (!AttribArray.update(this.instancesAttribs[c], atts[c]))
				GlobalLogger.log(Level.WARNING, "Failed to update attrib array: " + this.instancesAttribs[c].getName() + " (" + this.instancesAttribs[c].getIndex() + ")");
			/*else
				GlobalLogger.info("Updated attrib array: " + this.instancesAttribs[c].getName() + " (" + this.instancesAttribs[c].getIndex() + ") for " + this.getId());*/
		}
		GL_W.glBindBuffer(GL_W.GL_ARRAY_BUFFER, 0);
	}

	public void updateParticles() {
		Matrix4f[] transforms = new Matrix4f[this.count];
		Object[][] atts = new Object[this.instancesAttribs.length][this.count];

		for (int i = 0; i < this.count; i++) {
			transforms[i] = this.particles[i].getTransform().getMatrix();

			for (int c = 0; c < this.instancesAttribs.length; c++) {
				atts[c][i] = this.particles[i].getBuffers()[c];
			}
		}

		if (!AttribArray.update(this.instancesTransforms, transforms)) {
			GlobalLogger.log(Level.WARNING, "Could not update transforms");
		}

		for (int c = 0; c < this.instancesAttribs.length; c++) {
			if (!AttribArray.update(this.instancesAttribs[c], atts[c])) {
				GlobalLogger.log(Level.WARNING, "Failed to update attrib array: " + this.instancesAttribs[c].getName());
			}
		}
		GL_W.glBindBuffer(GL_W.GL_ARRAY_BUFFER, 0);
	}
	

	public void updateParticlesTransforms() {
		Matrix4f[] transforms = new Matrix4f[this.count];

		for (int i = 0; i < this.count; i++) {
			transforms[i] = this.particles[i].getTransform().getMatrix();
		}

		if (!AttribArray.update(this.instancesTransforms, transforms)) {
			GlobalLogger.log(Level.WARNING, "Could not update transforms");
		}

		GL_W.glBindBuffer(GL_W.GL_ARRAY_BUFFER, 0);
	}

	public void bind() {
		this.instanceMesh.bind();
	}

	public void unbind() {
		this.instanceMesh.unbind();
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: " + name);

		if (instanceMesh == null)
			return;

		Arrays.stream(this.instancesAttribs).forEach(AttribArray::cleanup);
		this.instancesAttribs = null;

		this.instancesTransforms.cleanup();
		this.instancesTransforms = null;

		this.instanceMesh.cleanup();
		this.instanceMesh = null;
	}
	
	@Override
	public String getId() {
		return this.name;
	}

	public int getParticleCount() {
		return this.count;
	}

	public Mesh getParticleMesh() {
		return this.instanceMesh;
	}

	public Instance[] getParticles() {
		return particles;
	}

	public AttribArray[] getParticleAttribs() {
		return this.instancesAttribs;
	}

	public AttribArray getParticleTransforms() {
		return this.instancesTransforms;
	}

}
