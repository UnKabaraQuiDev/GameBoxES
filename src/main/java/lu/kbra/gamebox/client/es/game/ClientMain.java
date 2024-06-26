package lu.kbra.gamebox.client.es.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;

import lu.kbra.gamebox.client.es.engine.GameEngine;
import lu.kbra.gamebox.client.es.engine.graph.window.WindowOptions;
import lu.kbra.gamebox.client.es.game.game.GameBoxES;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalOptions;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

public class ClientMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println(PCUtils.recursiveTree("./resources"));

		GlobalLogger.init(new File("./config/logs.properties"));

		GlobalLogger.log(Level.INFO, "Starting...");
		
		long start = System.currentTimeMillis();
		
		try {
			GlobalOptions.load();
			GlobalLogger.log("Loaded lang: " + GlobalOptions.LANGUAGE + " gets: " + GlobalLang.LANGUAGES[GlobalOptions.LANGUAGE]);
			GlobalLang.load(GlobalLang.LANGUAGES[GlobalOptions.LANGUAGE]);
			GlobalLogger.log("Loaded volume: " + GlobalLang.get("menu.options.volume"));
		} catch (Exception e1) {
			GlobalLogger.log(e1);
			System.exit(-1);
		}

		try {

			final WindowOptions options = new WindowOptions();
			options.fullscreen = false;
			options.vsync = false;
			options.fps = 60;
			options.windowMultisample = 0;
			options.title = "GameBoxES";
			options.gles = GlobalOptions.GLES;

			final GameBoxES gbes = new GameBoxES();

			final GameEngine engine = new GameEngine("GameBoxES", gbes, options);
			engine.start();

			// when quitting:
			gbes.eventStop();

		} catch (Exception e) {
			GlobalLogger.log(e);
			System.exit(-1);
		}

		GlobalLogger.log(Level.INFO, "Stopped after " + (double) (System.currentTimeMillis() - start) / 1000 + "s");

	}

}
