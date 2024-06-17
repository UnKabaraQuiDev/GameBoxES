package lu.kbra.gamebox.client.es.game.game.data;

import lu.kbra.gamebox.client.es.engine.utils.file.FileUtils;

public enum CellType {

	PLAYER("player/"), VIRUS("virus/"), PLAGUE("plague/"), MUSHROOM("mushroom/"), FLU("flu/");

	private String dataPath;

	private CellType(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getDataPath() {
		return dataPath;
	}

	public String getTexturePath() {
		return FileUtils.RESOURCES + "gd/cells/" + dataPath;
	}

}
