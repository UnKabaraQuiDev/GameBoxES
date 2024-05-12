package lu.kbra.gamebox.client.es.game.game.scenes.world.entities;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.joml.Vector2f;

import lu.kbra.gamebox.client.es.engine.cache.CacheManager;
import lu.kbra.gamebox.client.es.engine.graph.material.Material;
import lu.kbra.gamebox.client.es.engine.utils.MathUtils;
import lu.kbra.gamebox.client.es.engine.utils.consts.TextureFilter;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellInstanceShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellInstanceShader.CellInstanceMaterial;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellShader;
import lu.kbra.gamebox.client.es.game.game.render.shaders.CellShader.CellMaterial;

public class CellDescriptor {

	private CellType cellType;
	private String id;
	private String scientificName;

	private Vector2f hostilityRange, fertilityRange, humidityRange;
	private int variationCount;

	public CellDescriptor(String id, CellType cellType, String scientificName, Vector2f hostilityRange, Vector2f fertilityRange, Vector2f humidityRange, int variationCount) {
		this.id = id;

		this.cellType = cellType;
		this.scientificName = scientificName;

		this.hostilityRange = hostilityRange;
		this.fertilityRange = fertilityRange;
		this.humidityRange = humidityRange;
		
		this.variationCount = variationCount;
	}

	public boolean match(float hostility, float fertility, float humidity) {
		return MathUtils.rangeContains(hostility, hostilityRange) && MathUtils.rangeContains(fertility, fertilityRange) && MathUtils.rangeContains(humidity, humidityRange);
	}

	public CellInstanceMaterial loadOrGetMaterial(CacheManager cache) {
		if (cache == null)
			throw new IllegalArgumentException("CacheManager == null");

		final String materialName = cellType.name() + "-" + id;

		if (cache.hasMaterial(materialName)) {
			return (CellInstanceMaterial) cache.getMaterial(materialName);
		}

		String imagePath = cellType.getTexturePath() + id + ".png";

		if (!Files.exists(Paths.get(imagePath))) {
			throw new RuntimeException(new FileNotFoundException("Couln't find file: " + imagePath));
		}

		CellInstanceShader shader = (CellInstanceShader) cache.getRenderShader(CellInstanceShader.NAME);
		if (shader == null) {
			shader = new CellInstanceShader();
			cache.addRenderShader(shader);
		}
		
		System.err.println("image path: "+imagePath);

		CellInstanceMaterial material = new CellInstanceMaterial(cellType.name(), shader, cache.loadSingleTexture(materialName, imagePath, TextureFilter.NEAREST));
		cache.addMaterial(material);

		return material;
	}
	
	@Deprecated
	public Material createMaterial(CacheManager cache) {
		if (cache == null)
			throw new IllegalArgumentException("CacheManager == null");

		final String shaderName = cellType.name() + id;

		if (cache.hasMaterial(shaderName)) {
			return (CellMaterial) cache.getMaterial(shaderName);
		}

		String imagePath = cellType.getTexturePath() + id + ".png";

		if (!Files.exists(Paths.get(imagePath))) {
			throw new RuntimeException(new FileNotFoundException("Couln't find file: " + imagePath));
		}

		CellShader shader = (CellShader) cache.getRenderShader(CellShader.NAME);
		if (shader == null) {
			shader = new CellShader();
			cache.addRenderShader(shader);
		}

		CellMaterial material = new CellMaterial(cellType.name(), shader, cache.loadSingleTexture(shaderName, imagePath, TextureFilter.NEAREST));
		cache.addMaterial(material);

		return material;
	}

	public String getId() {
		return id;
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
	
	public int getTextureVariationCount() {
		return variationCount;
	}
	
}
