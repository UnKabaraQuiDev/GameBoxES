package lu.pcy113.pdr.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.client.game.PDRClientGame2;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;

public class ClientMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		System.out.println(Arrays.toString(new File("./config/").list()));

		GlobalLogger.init(new File("./config/logs.properties"));

		GlobalLogger.log(Level.INFO, "Starting");

		long start = System.currentTimeMillis();

		try {

			WindowOptions options = new WindowOptions();
			options.fullscreen = false;
			// PDRClientGame game = new PDRClientGame();
			GameEngine engine = new GameEngine(new PDRClientGame2(), options);
			engine.start();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		GlobalLogger.log(Level.INFO, "Stopped after " + (System.currentTimeMillis() - start) + "ms");

	}

}
