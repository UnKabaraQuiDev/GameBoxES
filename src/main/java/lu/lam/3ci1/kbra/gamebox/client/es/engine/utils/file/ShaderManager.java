package lu.pcy113.pdr.engine.utils.file;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;

import lu.pcy113.pdr.engine.cache.CacheManager;
import lu.pcy113.pdr.engine.cache.SharedCacheManager;
import lu.pcy113.pdr.engine.impl.shader.AbstractShader;
import lu.pcy113.pdr.engine.impl.shader.AbstractShaderPart;

public class ShaderManager {

	private CacheManager cache;
	private Path root;

	private WatchService watchService;
	private HashMap<String, AbstractShader> shaders = new HashMap<>();

	public ShaderManager(SharedCacheManager cache, String dir) throws IOException {
		this.cache = cache;
		this.root = Paths.get(dir);

		try {
			watchService = FileSystems.getDefault().newWatchService();
			// root.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY,
			// StandardWatchEventKinds.ENTRY_CREATE);
			Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					dir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			throw e;
		}
	}

	public void monitorShader(AbstractShader shader) {
		for (AbstractShaderPart part : shader.getParts().values()) {
			shaders.put(part.getFile(), shader);
		}
	}

	public void manageEvents() {
		while (true) {
			WatchKey key = watchService.poll();
			if (key == null)
				return;

			for (WatchEvent<?> event : key.pollEvents()) {

				if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY || event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();
					Path child = ((Path) key.watchable()).resolve(filename);
					System.err.println("File modified: " + filename + " and " + child);

					if (shaders.containsKey(child.toString())) {
						System.err.println("Recompiling shader: " + child);
						shaders.get(child.toString()).recompile();
					}
				}
			}

			key.reset();
		}
	}

}
