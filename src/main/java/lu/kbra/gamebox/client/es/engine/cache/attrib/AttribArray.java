package lu.kbra.gamebox.client.es.engine.cache.attrib;

import org.joml.Vector3f;
import org.joml.Vector4f;

import lu.kbra.gamebox.client.es.engine.impl.Cleanupable;
import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.engine.utils.gl.wrapper.GL_W;
import lu.pcy113.pclib.logger.GlobalLogger;

public abstract class AttribArray implements Cleanupable {

	protected int bufferIndex = -1;
	protected boolean iStatic = true;;

	protected int bufferType = -1;

	protected String name = null;
	protected int index = -1;
	protected final int dataSize, divisor;

	public AttribArray(String name, int index, int dataSize) {
		this(name, index, dataSize, GL_W.GL_ARRAY_BUFFER, true);
	}

	public AttribArray(String name, int index, int dataSize, int bufferType) {
		this(name, index, dataSize, bufferType, true);
	}

	public AttribArray(String name, int index, int dataSize, boolean iStatic) {
		this(name, index, dataSize, GL_W.GL_ARRAY_BUFFER, iStatic);
	}

	public AttribArray(String name, int index, int dataSize, boolean iStatic, int divisor) {
		this(name, index, dataSize, GL_W.GL_ARRAY_BUFFER, iStatic, divisor);
	}

	public AttribArray(String name, int index, int dataSize, int bufferType, boolean iStatic) {
		this(name, index, dataSize, bufferType, iStatic, 0);
	}

	public AttribArray(String name, int index, int dataSize, int bufferType, boolean iStatic, int divisor) {
		this.name = name;
		this.index = index;
		this.dataSize = dataSize;
		this.bufferType = bufferType;
		this.iStatic = iStatic;
		this.divisor = divisor;
	}

	public int getDataCount() {
		return getLength() / getDataSize();
	}

	public abstract int getLength();

	public abstract void init();

	public abstract Object get(int i);

	public void enable() {
		GL_W.glEnableVertexAttribArray(index);
		PDRUtils.checkGL_WError("EnableVertexAttribArray(" + index + ") (" + name + ")");
		GL_W.glVertexAttribDivisor(index, divisor);
		PDRUtils.checkGL_WError("VertexAttribDivisor(" + index + ", " + divisor + ") (" + name + ")");
	}

	public void disable() {
		GL_W.glDisableVertexAttribArray(index);
		PDRUtils.checkGL_WError("DisableVertexAttribArray(" + index + ") (" + name + ")");
	}

	public int gen() {
		return (bufferIndex = GL_W.glGenBuffers());
	}

	public void bind() {
		GL_W.glBindBuffer(bufferType, bufferIndex);
		PDRUtils.checkGL_WError("BindBuffer(" + bufferType + ", " + bufferIndex + ") (" + name + ")");
	}

	public void unbind() {
		GL_W.glBindBuffer(bufferType, 0);
		PDRUtils.checkGL_WError("BindBuffer(" + bufferType + ", 0) (" + name + ")");
	}

	@Override
	public void cleanup() {
		GlobalLogger.log("Cleaning up: "+name+" ("+index+"="+bufferIndex+")");
		
		if (bufferIndex == -1)
			return;

		GL_W.glDeleteBuffers(bufferIndex);
		PDRUtils.checkGL_WError("DeleteBuffers(" + bufferIndex + ") (" + name + ")");
		bufferIndex = -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getDataSize() {
		return dataSize;
	}

	public int getBufferType() {
		return bufferType;
	}

	public void setBufferType(int bufferType) {
		this.bufferType = bufferType;
	}

	/*
	 * public void setVbo(int bufferIndex) { this.bufferIndex = bufferIndex; }
	 */

	public int getBufferIndex() {
		return bufferIndex;
	}

	public boolean isStatic() {
		return iStatic;
	}

	public int getDivisor() {
		return divisor;
	}

	@Override
	public String toString() {
		return getBufferIndex() + "|" + getIndex() + ") " + getName() + ": " + getLength() + "/" + getDataSize() + "=" + getDataCount();
	}

	public static <T> boolean update(AttribArray arr, T[] data) {
		if(arr == null) {
			GlobalLogger.log();
			GlobalLogger.warning("AttribArray is null!");
			return false;
		}
		
		arr.bind();
		
		if (arr instanceof IntAttribArray)
			return ((IntAttribArray) arr).update(PDRUtils.toPrimitiveInt(data));
		else if (arr instanceof UIntAttribArray)
			return ((UIntAttribArray) arr).update(PDRUtils.toPrimitiveInt(data));
		else if (arr instanceof FloatAttribArray)
			return ((FloatAttribArray) arr).update(PDRUtils.toPrimitiveFloat(data));
		else if (arr instanceof Mat4fAttribArray)
			return ((Mat4fAttribArray) arr).update(PDRUtils.castArrayMat4f(data));
		else if (arr instanceof Vec4fAttribArray)
			return ((Vec4fAttribArray) arr).update((Vector4f[]) data);
		else if (arr instanceof Vec3fAttribArray)
			return ((Vec3fAttribArray) arr).update((Vector3f[]) data);
		else if (arr instanceof Mat3x2fAttribArray)
			return ((Mat3x2fAttribArray) arr).update(PDRUtils.castArrayMat3x2f(data));
		return false;
	}

}
