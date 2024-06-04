package lu.kbra.gamebox.client.es.game.game.utils.global;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public final class GlobalLang {
	
	public static final String[] LANGUAGES = new String[] { "English", "Francais" };
	
	private static Properties properties;
	private static String CURRENT_LANG;
	
	public static void load(String lang) throws FileNotFoundException, IOException {
		CURRENT_LANG = lang.toLowerCase();
		
		if(properties != null) {
			properties.clear();
			properties = null;
		}
		
		properties = new Properties();
		properties.load(new FileReader(new File("./resources/gd/lang/" + CURRENT_LANG + ".properties")));
	}
	
	public static String get(String key) {
		return ((String) properties.getOrDefault(key, key)).replace("<br>", "\n");
	}
	
	public static String getCURRENT_LANG() {
		return CURRENT_LANG;
	}

	public static String getLongestLang() {
		return Arrays.stream(LANGUAGES).sorted((String a, String b) -> b.length() - a.length()).findFirst().orElseGet(null);
	}
	
}
