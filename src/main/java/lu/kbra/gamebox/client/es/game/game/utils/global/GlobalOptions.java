package lu.kbra.gamebox.client.es.game.game.utils.global;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import lu.pcy113.pclib.logger.GlobalLogger;

public final class GlobalOptions {

	public static String LOGS_MIN_FORWARD_LEVEL = "info";
	public static boolean DEBUG = false;
	public static boolean WIREFRAME = false;
	public static boolean GIZMOS = false;
	public static int VOLUME = 5;
	public static int LANGUAGE = 0;
	public static boolean CULLING_OPTIMISATION = true;
	public static boolean LOGS_FORWARD = true;
	public static boolean GLES = false;

	public static void load() throws IOException {
		File defaultFile = new File("resources/gd/config/default_options.properties");
		Properties defaultProp = new Properties();
		defaultProp.load(new FileReader(defaultFile));

		Properties prop = new Properties(defaultProp);
		File file = new File("config/options.properties");
		if (!file.exists()) {
			file.createNewFile();
			prop.store(new FileWriter(file), null);
		}
		prop.load(new FileReader(file));

		VOLUME = Integer.parseInt(prop.getProperty("volume"));
		LANGUAGE = Integer.parseInt(prop.getProperty("language"));
		CULLING_OPTIMISATION = Boolean.parseBoolean(prop.getProperty("culling"));
		DEBUG = Boolean.parseBoolean(prop.getProperty("debug"));
		GIZMOS = Boolean.parseBoolean(prop.getProperty("gizmos"));
		WIREFRAME = Boolean.parseBoolean(prop.getProperty("wireframe"));
		LOGS_MIN_FORWARD_LEVEL = prop.getProperty("logs.forward.min_level");
		LOGS_FORWARD = Boolean.parseBoolean(prop.getProperty("logs.forward"));
		GLES = Boolean.parseBoolean(prop.getProperty("gles"));
	}

	public static void save() throws IOException {
		GlobalLogger.info("Saving options");

		Properties prop = new Properties();
		prop.setProperty("volume", VOLUME + "");
		prop.setProperty("language", LANGUAGE + "");
		prop.setProperty("culling", CULLING_OPTIMISATION + "");
		prop.setProperty("debug", DEBUG + "");
		prop.setProperty("wireframe", WIREFRAME + "");
		prop.setProperty("gizmos", GIZMOS + "");
		prop.setProperty("logs.forward.min_level", LOGS_MIN_FORWARD_LEVEL);
		prop.setProperty("logs.forward", LOGS_FORWARD + "");
		prop.setProperty("gles", GLES + "");

		File file = new File("config/options.properties");
		prop.store(new FileWriter(file), null);
	}

}
