package lu.pcy113.pdr.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.logging.Level;

import lu.pcy113.pclib.GlobalLogger;
import lu.pcy113.pdr.client.game.PDRClientGame2;
import lu.pcy113.pdr.engine.GameEngine;
import lu.pcy113.pdr.engine.graph.window.WindowOptions;

public class ClientMain {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		System.out.println(Arrays.toString(new File("./config/").list()));

		redirectStdErr();

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

	public static void redirectStdErr() {
		// Create a pipe
		PipedOutputStream pipeOut = new PipedOutputStream();
		PipedInputStream pipeIn;
		try {
			pipeIn = new PipedInputStream(pipeOut);
			System.setErr(new PrintStream(pipeOut));

			final FileWriter writer = new FileWriter("./logs/error.log", false);
			writer.write("started\n");
			Thread readerThread = new Thread(() -> {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(pipeIn))) {
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println("Stderr: " + line);
						writer.write("trace: " + System.currentTimeMillis() + line + "\n");
						writer.flush();
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}, "Reader Thread");
			readerThread.start();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				try {
					writer.write("ended\n");
					writer.flush();
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
