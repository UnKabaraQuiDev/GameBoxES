package lu.kbra.gamebox.client.es.game.game.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class GlobalLang {
	
	private static Properties properties;
	private static String CURRENT_LANG;
	
	public static void load(String lang) throws FileNotFoundException, IOException {
		CURRENT_LANG = lang;
		
		if(properties != null) {
			properties.clear();
			properties = null;
		}
		
		properties = new Properties();
		properties.load(new FileReader(new File("./resources/gd/lang/" + lang + ".properties")));
	}
	
	public static String get(String key) {
		return (String) properties.getOrDefault(key, "err_not_found: "+key);
	}
	
}
