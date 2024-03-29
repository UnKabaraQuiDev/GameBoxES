package lu.kbra.gamebox.client.es.game.game.scenes;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;
import lu.kbra.gamebox.client.es.engine.utils.mem.img.MemImage;
import lu.kbra.gamebox.client.es.game.game.scenes.CellShader.CellMaterial;

public enum CellType {

	PLAYER("player/", "player.png"), CELL1("cell1/", "txt.png");

	private String texturePath;
	private String dataPath;

	private CellType(String dataPath, String texturePath) {
		this.dataPath = dataPath;
		this.texturePath = texturePath;
	}

	public String getDataPath() {
		return dataPath;
	}

	public String getTexturePath() {
		return FileUtils.RESOURCES + "gd/" + dataPath + texturePath;
	}

	public CellMaterial createMaterial(CacheManager cache) {
		if (cache == null)
			throw new IllegalArgumentException("CacheManager == null");

		if (cache.hasMaterial(this.name())) {
			return (CellMaterial) cache.getMaterial(this.name());
		}
		
		String imagePath = getTexturePath();
		
		if(!Files.exists(Paths.get(imagePath))) {
			throw new RuntimeException(new FileNotFoundException("Couln't find file: "+imagePath));
		}
		
		/*MemImage image = FileUtils.STBILoadRGBA(imagePath);
		if(image == null) {
			throw new IllegalArgumentException("Couln't load image for: "+this);
		}*/
		
		CellShader shader = (CellShader) cache.getRenderShader(CellShader.NAME);
		if (shader == null) {
			shader = new CellShader();
			cache.addRenderShader(shader);
		}

		CellMaterial material = new CellMaterial(
				this.name(),
				shader,
				cache.loadSingleTexture(this.name(), imagePath, TextureFilter.NEAREST, TextureType.TXT2D));
		cache.addMaterial(material);

		return material;
	}

}
