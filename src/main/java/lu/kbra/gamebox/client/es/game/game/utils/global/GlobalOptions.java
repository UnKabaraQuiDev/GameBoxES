package lu.kbra.gamebox.client.es.game.game.utils.global;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import lu.pcy113.pclib.logger.GlobalLogger;

public final class GlobalOptions {
	
	public static int VOLUME = 5;
	public static int LANGUAGE = 0;
	public static boolean CULLING_OPTIMISATION = true;
	
	public static void load() throws IOException {
		File defaultFile = new File("resources/gd/config/default_options.properties");
		Properties defaultProp = new Properties();
		defaultProp.load(new FileReader(defaultFile));
		
		Properties prop = new Properties(defaultProp);
		File file = new File("config/options.properties");
		if(!file.exists()) {
			file.createNewFile();
			prop.store(new FileWriter(file), null);
		}
		prop.load(new FileReader(file));
		
		VOLUME = Integer.parseInt(prop.getProperty("volume"));
		LANGUAGE = Integer.parseInt(prop.getProperty("language"));
		CULLING_OPTIMISATION = Boolean.parseBoolean(prop.getProperty("culling"));
	}
	
	public static void save() throws IOException {
		GlobalLogger.info("Saving options");
		
		Properties prop = new Properties();
		prop.setProperty("volume", VOLUME+"");
		prop.setProperty("language", LANGUAGE+"");
		prop.setProperty("culling", CULLING_OPTIMISATION+"");
		
		File file = new File("config/options.properties");
		prop.store(new FileWriter(file), null);
	}
	
}
