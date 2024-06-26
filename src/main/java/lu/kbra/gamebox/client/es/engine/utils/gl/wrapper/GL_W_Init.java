package lu.kbra.gamebox.client.es.engine.utils.gl.wrapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL40;
import org.lwjgl.opengles.GLES30;

public final class GL_W_Init {

	private static final String BASE_CONTENT = "package lu.kbra.gamebox.client.es.engine.utils;\n" + "\n" + "public class GL_W {\n" + "\n";
	private static final String INSTANCE_CONTENT = "package lu.kbra.gamebox.client.es.engine.utils;\n" + "\n" + "public class {name} implements GL_W_Call {\n" + "\n";
	private static final String CALLER_CONTENT = "package lu.kbra.gamebox.client.es.engine.utils;\n" + "\n" + "public interface GL_W_Call {\n";
	private static final List<String> NAME_BLACKLIST = Arrays.asList("wait", "toString", "getClass", "notify", "notifyAll", "equals", "hashCode");

	public static void main(String[] args) throws IOException {
		callerBase();
		base();
		gles30();
		gl40();
	}

	private static void gl40() throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W_GL40.java";

		List<String> out = Arrays.stream(INSTANCE_CONTENT.replace("{name}", "GL_W_GL40").split("\n")).collect(Collectors.toList());

		out.add("public void init() {");
		out.add("GL_W.WRAPPER = this;");

		for (Field f : GL40.class.getFields()) {
			if (f.getType() == int.class) {
				out.add("GL_W." + f.getName() + " = GL40." + f.getName() + ";");
			}
		}

		out.add("}");

		final List<String> methods = new ArrayList<>();

		for (Method m : GL40.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;
			
			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append("@Override ");
			content.append("public " + ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			content.append(") {");

			content.append((!"void".equals(ret) ? "return " : "") + "GL40." + m.getName() + "(");

			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].getName() + (i != arrCount - 1 ? ", " : ""));
			}

			content.append(");");

			content.append("}");

			out.add(content.toString());
			methods.add(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}

		for (Method m : GLES30.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;
			
			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append("@Override ");
			content.append("public " + ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			content.append(") {");
			content.append("throw new RuntimeException(\"Not implemented by GL40.\");");
			content.append("}");

			if (methods.contains(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")"))
				continue;

			out.add(content.toString());
			methods.add(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}

		out.add("}");

		Files.write(Paths.get(FILE_PATH), out);
	}

	private static void callerBase() throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W_Call.java";

		List<String> out = Arrays.stream(CALLER_CONTENT.split("\n")).collect(Collectors.toList());

		final List<String> names = new ArrayList<String>();

		for (Method m : GLES30.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append(ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			content.append(");");

			names.add(content.toString());
			out.add(content.toString());
		}

		for (Method m : GL40.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append(ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			content.append(");");

			if (names.contains(content.toString()))
				continue;

			names.add(content.toString());
			out.add(content.toString());
		}

		out.add("}");

		Files.write(Paths.get(FILE_PATH), out);
	}

	private static void gles30() throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W_GLES30.java";

		List<String> out = Arrays.stream(INSTANCE_CONTENT.replace("{name}", "GL_W_GLES30").split("\n")).collect(Collectors.toList());

		out.add("public void init() {");
		out.add("GL_W.WRAPPER = this;");

		for (Field f : GLES30.class.getFields()) {
			if (f.getType() == int.class) {
				out.add("GL_W." + f.getName() + " = GLES30." + f.getName() + ";");
			}
		}

		out.add("}");

		final List<String> methods = new ArrayList<>();

		for (Method m : GLES30.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append("@Override ");
			content.append("public " + ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			content.append(") {");

			content.append((!"void".equals(ret) ? "return " : "") + "GLES30." + m.getName() + "(");

			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].getName() + (i != arrCount - 1 ? ", " : ""));
			}

			content.append(");");

			content.append("}");

			out.add(content.toString());
			methods.add(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}

		for (Method m : GL40.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append("@Override ");
			content.append("public " + ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				content.append(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			content.append(") {");
			content.append("throw new RuntimeException(\"Not implemented by GLES30.\");");
			content.append("}");

			if (methods.contains(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")"))
				continue;

			out.add(content.toString());
			methods.add(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}

		out.add("}");

		Files.write(Paths.get(FILE_PATH), out);
	}

	private static void base() throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W.java";

		List<String> out = Arrays.stream(BASE_CONTENT.split("\n")).collect(Collectors.toList());

		out.add("public static GL_W_Call WRAPPER;");

		final List<String> fields = new ArrayList<String>();

		for (Field f : GLES30.class.getFields()) {
			if (f.getType() == int.class) {
				out.add("public static int " + f.getName() + ";");
				fields.add(f.getName());
			}
		}

		for (Field f : GL40.class.getFields()) {
			if (fields.contains(f.getName()))
				continue;

			if (f.getType() == int.class) {
				out.add("public static int " + f.getName() + ";");
				fields.add(f.getName());
			}
		}

		final List<String> names = new ArrayList<String>();

		for (Method m : GLES30.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			out.add("public static " + ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				out.add(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			out.add(") {");

			out.add((!"void".equals(ret) ? "return " : "") + "WRAPPER." + m.getName() + "(");

			for (int i = 0; i < arrCount; i++) {
				out.add(m.getParameters()[i].getName() + (i != arrCount - 1 ? ", " : ""));
			}

			out.add(");");

			out.add("}");

			names.add(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}

		for (Method m : GL40.class.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			if (names.contains(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")"))
				continue;

			out.add("public static " + ret + " " + m.getName() + "(");
			int arrCount = m.getParameterCount();
			for (int i = 0; i < arrCount; i++) {
				out.add(m.getParameters()[i].toString() + (i != arrCount - 1 ? ", " : ""));
			}
			out.add(") {");

			out.add((!"void".equals(ret) ? "return " : "") + "WRAPPER." + m.getName() + "(");

			for (int i = 0; i < arrCount; i++) {
				out.add(m.getParameters()[i].getName() + (i != arrCount - 1 ? ", " : ""));
			}

			out.add(");");

			out.add("}");

			names.add(ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}

		out.add("}");

		Files.write(Paths.get(FILE_PATH), out);
	}

	private static String getType(Class<?> returnType) {
		if (returnType == int.class)
			return "int";
		else if (returnType == boolean.class)
			return "boolean";
		else if (returnType == void.class)
			return "void";
		else if (returnType == long.class)
			return "long";
		else if (returnType == float.class)
			return "float";
		else if (returnType == double.class)
			return "double";
		else if (returnType == short.class)
			return "short";
		else if (returnType == char.class)
			return "char";
		else if (returnType == ByteBuffer.class)
			return ByteBuffer.class.getName();
		else if (returnType == IntBuffer.class)
			return IntBuffer.class.getName();
		else if (returnType == ShortBuffer.class)
			return ShortBuffer.class.getName();
		else if (returnType == FloatBuffer.class)
			return FloatBuffer.class.getName();
		else if (returnType.isArray())
			return getType(returnType.getComponentType()) + "[]";
		else
			return returnType.getName();
	}

}
