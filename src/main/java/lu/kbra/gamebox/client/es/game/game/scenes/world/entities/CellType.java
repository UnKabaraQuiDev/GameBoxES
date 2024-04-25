package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;

public enum CellType {

	PLAYER("player/"), VIRUS("virus/"), BACTERIA("bacteria/");

	private String dataPath;

	private CellType(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getDataPath() {
		return dataPath;
	}

	public String getTexturePath() {
		return FileUtils.RESOURCES + "gd/" + dataPath;
	}

}
