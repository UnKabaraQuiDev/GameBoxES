package lu.kbra.gamebox.client.es;

import java.io.FileNotFoundException;
import java.io.IOException;

import lu.kbra.gamebox.client.es.game.game.data.PlayerData;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;

public class PrivateMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		GlobalLang.load(GlobalLang.LANGUAGES[0]);

		PlayerData pd = new PlayerData();
		
		System.out.println(pd.getTree().toString(0));
	}

}
