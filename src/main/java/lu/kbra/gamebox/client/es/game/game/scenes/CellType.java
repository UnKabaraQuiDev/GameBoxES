package lu.kbra.gamebox.client.es.game.game.scenes;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.texture.SingleTexture;
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
		if (cache.hasMaterial(this.name())) {
			return (CellMaterial) cache.getMaterial(this.name());
		}

		MemImage image = FileUtils.STBILoadRGBA(getDataPath() + getTexturePath());

		CellShader shader = (CellShader) cache.getRenderShader(CellShader.NAME);
		if (shader == null) {
			shader = new CellShader();
			cache.addRenderShader(shader);
		}

		CellMaterial material = new CellMaterial(this.name(), shader,
				new SingleTexture(this.name(), image.getWidth(), image.getHeight(), image));
		cache.addMaterial(material);

		return material;
	}

}
