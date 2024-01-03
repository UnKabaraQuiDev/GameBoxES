package lu.pcy113.pdr.engine.objs.text;

import java.util.Arrays;
import java.util.logging.Level;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.engine.cache.attrib.UByteAttribArray;
import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.graph.material.text.TextShader.TextMaterial;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.transform.Transform2D;

public class TextEmitter implements Cleanupable, UniqueID {

	public static final String NAME = TextEmitter.class.getName();

	public static float TAB_SIZE = 4;

	private final String name;

	private Vector2f charSize;
	private CharSequence text;

	private UIntAttribArray charBuffer;
	private InstanceEmitter instances;
	private Mesh quad;

	public TextEmitter(String name, TextMaterial material, int bufferSize, CharSequence text, Vector2f size) {
		this.name = name;

		this.text = text;
		this.charSize = size;

		Integer[] chars = new Integer[bufferSize];
		Arrays.fill(chars, 0);
		updateTextContent(new Matrix4f[bufferSize], chars);
		GlobalLogger.log(Level.FINEST, "SET: " + Arrays.toString(chars));

		this.charBuffer = new UIntAttribArray("char", 7, 1, PDRUtils.toPrimitiveInt(chars), false, 1);
		GlobalLogger.log(Level.FINEST, Arrays.toString(charBuffer.getData()));
		this.quad = Mesh.newQuad(name, material, size);

		this.instances = new InstanceEmitter(name, quad, bufferSize, new Transform2D(), charBuffer);
	}

	public boolean updateText() {
		if (charBuffer.getLength() < text.length())
			throw new RuntimeException("Char buffer too small to hold text.");

		TextMaterial material = (TextMaterial) quad.getMaterial();

		material.setProperty(TextShader.TXT_LENGTH, text.length());

		Matrix4f[] transforms = new Matrix4f[instances.getParticleCount()];
		Integer[] chars = new Integer[instances.getParticleCount()];
		Arrays.fill(chars, 0);

		updateTextContent(transforms, chars);

		GlobalLogger.log(Level.FINEST, Arrays.deepToString(transforms));

		instances.updateDirect(transforms, new Object[][] { chars });

		GlobalLogger.log(Level.FINEST, Arrays.toString(charBuffer.getData()));

		return true;
	}

	private void updateTextContent(Matrix4f[] transforms, Integer[] chars) {
		int line = 0;
		int character = 0;

		for (int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);

			if (currentChar == '\n') {
				line++;
				character = 0; // Reset character count for a new line
			} else if (currentChar == '\t') {
				character += 4; // Move 4 characters forward for a tab
			} else if (currentChar == ' ') {
				character++;
			} else {
				character++;
				chars[i] = (int) currentChar;
				
				float translationX = character * charSize.x; // Adjust as needed
				float translationY = line * charSize.y; // Adjust as needed

				transforms[i] = new Matrix4f().identity().translate(translationX, translationY, 0);
			}
		}
	}

	public CharSequence getText() {
		return text;
	}

	public void setText(CharSequence text) {
		this.text = text;
	}

	public InstanceEmitter getInstances() {
		return instances;
	}

	@Override
	public void cleanup() {
		instances.cleanup();
		instances = null;
		quad.cleanup();
		quad = null;
	}

	@Override
	public String getId() {
		return name;
	}

}
