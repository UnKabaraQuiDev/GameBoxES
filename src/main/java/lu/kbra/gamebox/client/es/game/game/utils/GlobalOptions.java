package lu.kbra.gamebox.client.es.game.game.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class GlobalOptions {
	
	public static int VOLUME = 5;
	
	public static void load() throws IOException {
		File defaultFile = new File("resources/gd/config/default_options.properties");
		
		Properties defaultProp = new Properties();
		defaultProp.load(new FileReader(defaultFile));
		
		Properties prop = new Properties(defaultProp);
		File file = new File("options.properties");
		if(!file.exists()) {
			file.createNewFile();
			prop.store(new FileWriter(file), null);
		}
		prop.load(new FileReader(file));
		
		VOLUME = Integer.parseInt(prop.getProperty("volume"));
	}
	
}
