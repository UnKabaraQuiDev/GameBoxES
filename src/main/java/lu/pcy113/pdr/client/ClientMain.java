package lu.pcy113.pdr.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import lu.pcy113.pdr.client.game.PDRClientGame;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;
import lu.pcy113.pdr.utils.Logger;

public class ClientMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		System.out.println(Arrays.toString(new File("./config/").list()));
		
		Logger.init(new File("./config/logs.properties"));
		
		Logger.log(Level.INFO, "Starting");
		
		long start = System.currentTimeMillis();
		
		try {
			
			WindowOptions options = new WindowOptions();
			options.fullscreen = true;
			PDRClientGame game = new PDRClientGame();
			GameEngine engine = new GameEngine(game, options);
			engine.start();
		
		}catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
			
		Logger.log(Level.INFO, "Stopped after "+(System.currentTimeMillis()-start)+"ms");
		
	}
	
}
