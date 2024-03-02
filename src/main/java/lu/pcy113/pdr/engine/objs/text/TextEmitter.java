package lu.pcy113.pdr.engine.objs.text;

import java.util.Arrays;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import lu.pcy113.pdr.engine.cache.attrib.UIntAttribArray;
import lu.pcy113.pdr.engine.geom.Mesh;
import lu.pcy113.pdr.engine.geom.instance.InstanceEmitter;
import lu.pcy113.pdr.engine.graph.material.text.TextShader;
import lu.pcy113.pdr.engine.graph.material.text.TextShader.TextMaterial;
import lu.pcy113.pdr.engine.impl.Cleanupable;
import lu.pcy113.pdr.engine.impl.UniqueID;
import lu.pcy113.pdr.engine.utils.PDRUtils;
import lu.pcy113.pdr.engine.utils.consts.Alignment;
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

	private Alignment alignment = Alignment.LEFT;
	private boolean justify = false, boxed = false;
	private Vector2f boxSize;

	public TextEmitter(String name, TextMaterial material, int bufferSize, CharSequence text, Vector2f size) {
		this.name = name;

		this.text = text;
		this.charSize = size;

		Integer[] chars = new Integer[bufferSize];
		Arrays.fill(chars, 0);
		updateTextContent(new Matrix4f[bufferSize], chars);
		// GlobalLogger.log(Level.FINEST, "SET: " + Arrays.toString(chars));

		this.charBuffer = new UIntAttribArray("char", 7, 1, PDRUtils.toPrimitiveInt(chars), false, 1);
		// GlobalLogger.log(Level.FINEST, Arrays.toString(charBuffer.getData()));
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

		// GlobalLogger.log(Level.FINEST, Arrays.deepToString(transforms));

		instances.updateDirect(transforms, new Object[][] { chars });

		// GlobalLogger.log(Level.FINEST, Arrays.toString(charBuffer.getData()));

		return true;
	}

	private void updateTextContent(Matrix4f[] transforms, Integer[] chars) {
		if (Alignment.LEFT.equals(alignment)) {
			updateTextContentLeft(transforms, chars);
		} else if (Alignment.RIGHT.equals(alignment)) {
			updateTextContentRight(transforms, chars);
		} else if (Alignment.CENTER.equals(alignment)) {
			// updateTextContentCenter(transforms, chars);
		}

	}

	// TODO
	private void updateTextContentRight(Matrix4f[] transforms, Integer[] chars) {
		int line = 0;
		int character = 0;

		Vector2f[] poss = new Vector2f[chars.length];

		float maxX = 0;

		int i = 0;
		for (; i < text.length();) {
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
				float translationY = -line * charSize.y; // Adjust as needed

				poss[i] = new Vector2f(translationX, translationY);

				maxX = Math.max(maxX, translationX);

				i++;
			}
		}

		System.out.println("text:" + text + " count: " + i);
		System.out.println(Arrays.toString(poss));
		for (int j = 0; j < i; j++) {
			System.out.println("Text lenght: " + text.length() + " index: " + j + " poss: " + poss[i]);

			transforms[j] = new Matrix4f().identity().translate(maxX - poss[j].x, poss[j].y, 0);
		}
	}

	private void updateTextContentLeft(Matrix4f[] transforms, Integer[] chars) {
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

	public TextEmitter setText(CharSequence text) {
		this.text = text;
		return this;
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
