package lu.kbra.gamebox.client.es.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.window.WindowOptions;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;

public class ClientMain {

	/*
	 * TODO: Add other font files x95 V: Add antialiasing to framebuffer renderlayer
	 * TODO: AttribArray resize TODO: utils.codec.* TODO: Compute shaders TODO:
	 * Compositor + Compute shader TODO: BATCH FOR SHADER
	 */

	public static void main(String[] args) throws FileNotFoundException, IOException {

		System.out.println(Arrays.toString(new File("./config/").list()));

		System.out.println(FileUtils.recursiveTree("./resources"));

		GlobalLogger.init(new File("./config/logs.properties"));

		GlobalLogger.log(Level.INFO, "Starting...");

		long start = System.currentTimeMillis();

		try {

			WindowOptions options = new WindowOptions();
			options.fullscreen = false;
			options.vsync = true;
			options.fps = 60;
			options.windowMultisample = 8;
			
			GameBoxES gbes = new GameBoxES();
			
			GameEngine engine = new GameEngine("PDRClientGame4", gbes, options);
			engine.start();
			
			// when quitting:
			gbes.eventStop();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		GlobalLogger.log(Level.INFO, "Stopped after " + (System.currentTimeMillis() - start) + "ms");

	}

}
