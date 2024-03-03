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

	public static int TAB_SIZE = 4;

	private final String name;

	private Vector2f charSize;
	private String text;

	private UIntAttribArray charBuffer;
	private InstanceEmitter instances;
	private Mesh quad;

	private Alignment alignment = Alignment.CENTER;
	private boolean justify = false, boxed = false;
	private Vector2f boxSize;

	public TextEmitter(String name, TextMaterial material, int bufferSize, String text, Vector2f size) {
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
		} else if (Alignment.ABSOLUTE_LEFT.equals(alignment)) { // same as LEFT
			updateTextContentLeft(transforms, chars);
		} else if (Alignment.RIGHT.equals(alignment)) {
			updateTextContentRight(transforms, chars);
		} else if (Alignment.ABSOLUTE_RIGHT.equals(alignment)) {
			updateTextContentAbsRight(transforms, chars);
		} else if (Alignment.CENTER.equals(alignment)) {
			updateTextContentCenter(transforms, chars);
		} else if (Alignment.ABSOLUTE_CENTER.equals(alignment)) {
			updateTextContentAbsCenter(transforms, chars);
		}

	}

	private void updateTextContentAbsCenter(Matrix4f[] transforms, Integer[] chars) {
		final int[] widthCount = computeWidthCounts();
		final int widthMax = boxed ? (int) (boxSize.x / charSize.x) : Arrays.stream(widthCount).max().getAsInt();

		int line = 0;
		int character = 0;

		int charIndex = 0;
		for (int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);

			if (currentChar == '\n') {
				line++;
				character = 0;
			} else if (currentChar == '\t') {
				character += TAB_SIZE;
			} else if (currentChar == ' ') {
				character++;
			} else {
				character++;
				chars[charIndex] = (int) currentChar;

				float translationX = (character - widthCount[line] / 2) * charSize.x;
				float translationY = line * charSize.y;

				transforms[charIndex] = new Matrix4f().identity().translate(translationX, translationY, 0);

				charIndex++;
			}
		}
	}

	private void updateTextContentCenter(Matrix4f[] transforms, Integer[] chars) {
		final int[] widthCount = computeWidthCounts();
		final float widthMax = boxed ? boxSize.x : Arrays.stream(widthCount).max().getAsInt() * charSize.x;

		int line = 0;
		int character = 0;

		int charIndex = 0;
		for (int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);

			if (currentChar == '\n') {
				line++;
				character = 0;
			} else if (currentChar == '\t') {
				character += TAB_SIZE;
			} else if (currentChar == ' ') {
				character++;
			} else {
				character++;
				chars[charIndex] = (int) currentChar;

				float translationX = (character - widthCount[line]/2) * charSize.x + widthMax/2;
				// character * charSize.x + ((widthMax - widthCount[line]*charSize.x) / 2);
				// c * z +((m-b*x)/2)
				float translationY = line * charSize.y;

				transforms[charIndex] = new Matrix4f().identity().translate(translationX, translationY, 0);

				charIndex++;
			}
		}
	}

	private void updateTextContentRight(Matrix4f[] transforms, Integer[] chars) {
		final int[] widthCount = computeWidthCounts();
		final int widthMax = Arrays.stream(widthCount).max().getAsInt();

		int line = 0;
		int character = 0;

		int charIndex = 0;
		for (int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);

			if (currentChar == '\n') {
				line++;
				character = 0;
			} else if (currentChar == '\t') {
				character += TAB_SIZE;
			} else if (currentChar == ' ') {
				character++;
			} else {
				character++;
				chars[charIndex] = (int) currentChar;

				float translationX = ((widthMax - widthCount[line]) + character) * charSize.x;
				float translationY = line * charSize.y;

				transforms[charIndex] = new Matrix4f().identity().translate(translationX, translationY, 0);

				charIndex++;
			}
		}
	}

	private void updateTextContentAbsRight(Matrix4f[] transforms, Integer[] chars) {
		final int[] widthCount = computeWidthCounts();
		final int widthMax = Arrays.stream(widthCount).max().getAsInt();

		int line = 0;
		int character = 0;

		int charIndex = 0;
		for (int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);

			if (currentChar == '\n') {
				line++;
				character = 0;
			} else if (currentChar == '\t') {
				character += TAB_SIZE;
			} else if (currentChar == ' ') {
				character++;
			} else {
				character++;
				chars[charIndex] = (int) currentChar;

				float translationX = (character - widthCount[line]) * charSize.x;
				float translationY = line * charSize.y;

				transforms[charIndex] = new Matrix4f().identity().translate(translationX, translationY, 0);

				charIndex++;
			}
		}
	}

	private void updateTextContentLeft(Matrix4f[] transforms, Integer[] chars) {
		int line = 0;
		int character = 0;

		int charIndex = 0;
		for (int i = 0; i < text.length(); i++) {
			char currentChar = text.charAt(i);

			if (currentChar == '\n') {
				line++;
				character = 0; // Reset character count for a new line
			} else if (currentChar == '\t') {
				character += TAB_SIZE; // Move 4 characters forward for a tab
			} else if (currentChar == ' ') {
				character++;
			} else {
				character++;
				chars[charIndex] = (int) currentChar;

				float translationX = character * charSize.x; // Adjust as needed
				float translationY = line * charSize.y; // Adjust as needed

				transforms[charIndex] = new Matrix4f().identity().translate(translationX, translationY, 0);

				charIndex++;
			}
		}
	}

	public int computeWidthCount() {
		int max = Integer.MIN_VALUE;

		for (String s : text.split("\n")) {
			max = Math.max(max, s.length());
		}

		return max;
	}

	public int[] computeWidthCounts() {
		String[] lines = text.split("\n");
		int[] max = new int[lines.length];

		for (int i = 0; i < lines.length; i++) {
			max[i] = lines[i].length();
		}

		return max;
	}

	public int getLineCount() {
		return text.length() - text.replace("\n", "").length();
	}

	public String getText() {
		return text;
	}

	public TextEmitter setText(String text) {
		this.text = text;
		return this;
	}

	public InstanceEmitter getInstances() {
		return instances;
	}

	public Alignment getAlignment() {
		return alignment;
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public boolean isJustify() {
		return justify;
	}

	public void setJustify(boolean justify) {
		this.justify = justify;
	}

	public boolean isBoxed() {
		return boxed;
	}

	public void setBoxed(boolean boxed) {
		this.boxed = boxed;
	}

	public Vector2f getBoxSize() {
		return boxSize;
	}

	public void setBoxSize(Vector2f boxSize) {
		this.boxSize = boxSize;
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
