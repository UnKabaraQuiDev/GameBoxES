package lu.kbra.gamebox.client.es.game.game.data;

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
	private float aggressivity, hardAggressiveDistance, softAggressiveDistance;

	public CellDescriptor(String id, CellType cellType, String scientificName, Vector2f hostilityRange, Vector2f fertilityRange, Vector2f humidityRange, int variationCount, float aggresiv, float hardAggresivDist, float softAggresivDist) {
		this.id = id;

		this.cellType = cellType;
		this.scientificName = scientificName;

		this.hostilityRange = hostilityRange;
		this.fertilityRange = fertilityRange;
		this.humidityRange = humidityRange;

		this.variationCount = variationCount;

		this.aggressivity = aggresiv;
		this.hardAggressiveDistance = hardAggresivDist;
		this.softAggressiveDistance = softAggresivDist;
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
			throw new RuntimeException(new FileNotFoundException("Couldn't find file: " + imagePath));
		}

		CellInstanceShader shader = (CellInstanceShader) cache.loadOrGetRenderShader(CellInstanceShader.NAME, CellInstanceShader.class);

		CellInstanceMaterial material = new CellInstanceMaterial(materialName, shader, cache.loadSingleTexture(materialName, imagePath, TextureFilter.NEAREST), variationCount);
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

	public Vector2f getHostilityRange() {
		return hostilityRange;
	}

	public Vector2f getFertilityRange() {
		return fertilityRange;
	}

	public Vector2f getHumidityRange() {
		return humidityRange;
	}

	public int getVariationCount() {
		return variationCount;
	}

	public float getAggressivity() {
		return aggressivity;
	}

	public float getHardAggressiveDistance() {
		return hardAggressiveDistance;
	}

	public float getSoftAggressiveDistance() {
		return softAggressiveDistance;
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()+"@"+hashCode()+"{"+cellType+", "+id+", "+scientificName+", "+hostilityRange+", "+fertilityRange+", "+humidityRange+", "+variationCount+", "+aggressivity+", "+hardAggressiveDistance+", "+softAggressiveDistance+"}";
	}

}
