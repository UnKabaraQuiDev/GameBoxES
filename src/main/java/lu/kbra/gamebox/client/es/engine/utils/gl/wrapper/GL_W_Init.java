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
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengles.GLES30;
import org.lwjgl.opengles.GLES32;

public final class GL_W_Init {

	private static final String BASE_CONTENT = "package lu.kbra.gamebox.client.es.engine.utils.gl.wrapper;\n" + "\n" + "public class GL_W {\n" + "\n";
	private static final String INSTANCE_CONTENT = "package lu.kbra.gamebox.client.es.engine.utils.gl.wrapper;\n" + "\n" + "public class {name} implements GL_W_Call {\n";
	private static final String CALLER_CONTENT = "package lu.kbra.gamebox.client.es.engine.utils.gl.wrapper;\n" + "\n" + "public interface GL_W_Call {\n";
	private static final List<String> NAME_BLACKLIST = Arrays.stream(Object.class.getDeclaredMethods()).map((c) -> c.getName()).distinct().collect(Collectors.toList()); // .asList("wait", "toString", "getClass", "notify", "notifyAll",
																																											// "equals", "hashCode");

	public static void main(String[] args) throws IOException {
		System.out.println("Filtering out: " + NAME_BLACKLIST);

		final List<Class<?>> allGLClasses = Arrays.asList(GL40.class, GLES30.class, GLES32.class, GL43.class, GL46.class);

		callerBase(allGLClasses);
		System.out.println("GL_W_Call done.");
		base(allGLClasses);
		System.out.println("GL_W done.");
		for (Class c : allGLClasses) {
			glInit(c, allGLClasses.stream().filter((a) -> !a.equals(c)).collect(Collectors.toList()));
			System.out.println("GL_W_" + c.getSimpleName() + " done.");
		}
	}

	private static void glInit(Class<?> clazz, List<Class<?>> otherClazzes) throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W_" + clazz.getSimpleName() + ".java";

		List<String> out = Arrays.stream(INSTANCE_CONTENT.replace("{name}", "GL_W_" + clazz.getSimpleName()).split("\n")).collect(Collectors.toList());

		out.add("\n\tpublic void init() {");
		out.add("\t\tGL_W.WRAPPER = this;");

		implementFields(clazz, out);

		out.add("\t}");

		final List<String> methods = new ArrayList<>();

		implementMethods(clazz, methods, out);

		for (Class<?> cla : otherClazzes) {
			implementMethods(cla, methods, out);
		}

		out.add("}");

		Files.write(Paths.get(FILE_PATH), out);
	}

	private static void implementFields(Class<?> clazz, List<String> out) {
		for (Field f : clazz.getFields()) {
			if (f.getType() == int.class) {
				out.add("\t\tGL_W." + f.getName() + " = " + clazz.getName() + "." + f.getName() + ";");
			}
		}
	}

	private static void implementMethods(Class<?> clazz, List<String> methods, List<String> out) {
		for (Method m : clazz.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append("\t@Override\n");
			content.append("\tpublic " + ret + " " + m.getName() + "(");
			content.append(Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")));
			content.append(") {");

			content.append("\n\t\t" + (!"void".equals(ret) ? "return " : "") + clazz.getName() + "." + m.getName() + "(");

			content.append(Arrays.stream(m.getParameters()).map((c) -> c.getName()).collect(Collectors.joining(",")));

			content.append(");\n");

			content.append("\t}\n");

			if (methods.contains(m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")"))
				continue;

			out.add(content.toString());
			methods.add(m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
		}
	}

	private static void callerBase(List<Class<?>> clazzes) throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W_Call.java";

		List<String> out = Arrays.stream(CALLER_CONTENT.split("\n")).collect(Collectors.toList());

		List<String> names = new ArrayList<String>();

		for (Class<?> clazz : clazzes) {
			names = implementAbstractMethods(clazz, out, names);
		}
		out.add("}");

		Files.write(Paths.get(FILE_PATH), out);
	}

	private static List<String> implementAbstractMethods(Class<?> clazz, List<String> out, List<String> names) {
		for (Method m : clazz.getMethods()) {
			if (NAME_BLACKLIST.contains(m.getName()))
				continue;

			String ret = getType(m.getReturnType());

			StringBuilder content = new StringBuilder();

			content.append("\tdefault " + ret + " " + m.getName() + "(");
			content.append(Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")));
			content.append(") {throw new RuntimeException(\"Not implemented yet.\");}\n");

			if (names.contains(m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")"))
				continue;

			names.add(m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
			out.add(content.toString());
		}

		return names;
	}

	private static void base(List<Class<?>> clazzes) throws IOException {
		final String FILE_PATH = "./src/main/java/lu/kbra/gamebox/client/es/engine/utils/gl/wrapper/GL_W.java";

		List<String> out = Arrays.stream(BASE_CONTENT.split("\n")).collect(Collectors.toList());

		out.add("\n\tpublic static GL_W_Call WRAPPER;\n");

		final List<String> fields = new ArrayList<String>();

		for (Class<?> clazz : clazzes) {
			for (Field f : clazz.getFields()) {
				if (fields.contains(f.getName()))
					continue;

				if (f.getType() == int.class) {
					out.add("\tpublic static int " + f.getName() + ";");
					fields.add(f.getName());
				}
			}
		}

		final List<String> names = new ArrayList<String>();

		for (Class<?> clazz : clazzes) {
			for (Method m : clazz.getMethods()) {
				if (NAME_BLACKLIST.contains(m.getName()))
					continue;

				String ret = getType(m.getReturnType());

				StringBuilder content = new StringBuilder();

				content.append("\t/** " + clazz.getSimpleName() + " */\n");
				content.append("\tpublic static " + ret + " " + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ") {\n");

				content.append("\t\t" + (!"void".equals(ret) ? "return " : "") + "WRAPPER." + m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.getName()).collect(Collectors.joining(",")) + ");\n");

				content.append("\t}\n\n");

				if (names.contains(m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")"))
					continue;

				out.add(content.toString());
				names.add(m.getName() + "(" + Arrays.stream(m.getParameters()).map((c) -> c.toString()).collect(Collectors.joining(",")) + ")");
			}
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
