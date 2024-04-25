package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureType;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellShader.CellMaterial;

public class CellDescriptor {

	private CellType cellType;
	private String scientificName;
	private String dataName;

	public CellDescriptor(CellType cellType, String scientificName, String dataName) {
		this.cellType = cellType;
		this.scientificName = scientificName;
		this.dataName = dataName;
	}

	public Material createMaterial(CacheManager cache) {
		if (cache == null)
			throw new IllegalArgumentException("CacheManager == null");

		final String shaderName = cellType.name() + dataName;

		if (cache.hasMaterial(shaderName)) {
			return (CellMaterial) cache.getMaterial(shaderName);
		}

		String imagePath = cellType.getTexturePath()+dataName+".png";

		if (!Files.exists(Paths.get(imagePath))) {
			throw new RuntimeException(new FileNotFoundException("Couln't find file: " + imagePath));
		}

		CellShader shader = (CellShader) cache.getRenderShader(CellShader.NAME);
		if (shader == null) {
			shader = new CellShader();
			cache.addRenderShader(shader);
		}

		CellMaterial material = new CellMaterial(cellType.name(), shader, cache.loadSingleTexture(cellType.name(), imagePath, TextureFilter.NEAREST, TextureType.TXT2D));
		cache.addMaterial(material);

		return material;
	}

	public CellType getCellType() {
		return cellType;
	}

	public void setCellType(CellType cellType) {
		this.cellType = cellType;
	}

	public String getScientificName() {
		return scientificName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

}
