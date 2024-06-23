package lu.kbra.gamebox.client.es.game.game.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONException;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.game.game.render.shaders.PlayerCellShader.PlayerCellMaterial;
import lu.kbra.gamebox.client.es.game.game.scenes.ui.UISceneGameOverlay;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class PlayerData {

	public static final float TIME_SCORE_MUL = 0.001f;
	public static final int START_HEALTH = 3, START_SPEED = 1;

	private int maxHealth = START_HEALTH, health = maxHealth;
	private int damage = 0;
	private int photosynthesis = 0;
	private int speed = START_SPEED;
	private boolean toxinResistant = false, predatorRepulsion = false, poisonDamage = false, poisonTrail = false;

	private int glucose = 0;
	private int aminoAcid = 0;
	/**
	 * To upgrade the cell aka health, from plants or other dead cells
	 */
	private int lipid = 0;

	private long startTime = 0;
	private long score = 0;

	private EvolutionTree tree;
	private EvolutionTreeNode currentTreeNode;

	private int ennemyKillCount = 0, upgradeTreeCount = 0;
	private List<Achievements> achievements = new ArrayList<>();

	public PlayerData() {
		try {
			tree = EvolutionTree.load();
			currentTreeNode = tree;
			GlobalLogger.info(tree.toString(0));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public void eatPlant() {
		unlockAchievement(Achievements.EAT_A_PLANT);

		int rand = (int) (Math.random() * 3);
		if (rand < 1) {
			glucose++;
		} else if (rand < 2) {
			aminoAcid++;
		} else if (rand < 3) {
			lipid++;
		}

		if (!hasAchievement(Achievements.OVERLOADED) && glucose > 9999 && aminoAcid > 9999 && lipid > 9999) {
			unlockAchievement(Achievements.OVERLOADED);
		}

		if (!hasAchievement(Achievements.HEAVY_POCKETS) && glucose >= 100 && aminoAcid >= 100 && lipid >= 100) {
			unlockAchievement(Achievements.HEAVY_POCKETS);
		}
	}

	public void eatCell() {
		for (int i = 0; i < 5; i++) {
			int rand = (int) (Math.random() * 3);
			if (rand < 1) {
				glucose++;
			} else if (rand < 2) {
				aminoAcid++;
			} else if (rand < 3) {
				lipid++;
			}
		}
	}

	public void damage(int damage) {
		this.health -= damage;
		((UISceneGameOverlay) GlobalUtils.INSTANCE.uiScene.getState()).startHealthEmpty();

		if (this.health <= 0) {
			GlobalUtils.triggerGameEndDeath();
		}
	}

	public void setGlucose(int glucose) {
		this.glucose = glucose;
	}

	public void setAminoAcid(int aminoAcid) {
		this.aminoAcid = aminoAcid;
	}

	public void setLipid(int lipid) {
		this.lipid = lipid;
	}

	public boolean canRestoreHealth() {
		return health < maxHealth && lipid >= getNextRestoreHealthPrice();
	}

	private int getNextRestoreHealthPrice() {
		return maxHealth - health;
	}

	public boolean restoreHealth() {
		if (canRestoreHealth()) {
			lipid -= getNextRestoreHealthPrice();
			health++;
			return true;
		}
		return false;
	}

	public EvolutionTreeNode getCurrentTreeNode() {
		return currentTreeNode;
	}

	public EvolutionTree getTree() {
		return tree;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getSpeed() {
		return speed;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getGlucose() {
		return glucose;
	}

	public int getAminoAcid() {
		return aminoAcid;
	}

	public int getLipid() {
		return lipid;
	}

	public List<Achievements> getAchievements() {
		return achievements;
	}

	public boolean hasAchievement(Achievements a) {
		return achievements.contains(a);
	}

	public void unlockAchievement(Achievements a) {
		if (!hasAchievement(a)) {
			GlobalLogger.info("Unlocked: " + a);
			achievements.add(a);
		}
	}

	public void setAchievements(List<Achievements> achievements) {
		this.achievements = achievements;
	}

	public int getEnnemyKillCount() {
		return ennemyKillCount;
	}

	public int addEnnemyKillCount() {
		return ennemyKillCount++;
	}

	public void setEnnemyKillCount(int ennemyKillCount) {
		this.ennemyKillCount = ennemyKillCount;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getPhotosynthesis() {
		return photosynthesis;
	}

	public void setPhotosynthesis(int photosynthesis) {
		this.photosynthesis = photosynthesis;
	}

	public boolean isToxinResistant() {
		return toxinResistant;
	}

	public void setToxinResistant(boolean toxinResistant) {
		this.toxinResistant = toxinResistant;
	}

	public int getUpgradeTreeCount() {
		return upgradeTreeCount;
	}

	public void setUpgradeTreeCount(int upgradeTreeCount) {
		this.upgradeTreeCount = upgradeTreeCount;
	}

	public boolean isPredatorRepulsion() {
		return predatorRepulsion;
	}

	public void setPredatorRepulsion(boolean predatorRepulsion) {
		this.predatorRepulsion = predatorRepulsion;
	}

	public boolean isPoisonDamage() {
		return poisonDamage;
	}

	public void setPoisonDamage(boolean poisonDamage) {
		this.poisonDamage = poisonDamage;
	}

	public boolean hasPoisonTrail() {
		return poisonTrail;
	}

	public void setPoisonTrail(boolean poisonTrail) {
		this.poisonTrail = poisonTrail;
	}

	public static void main(String[] args) {
		try {
			GlobalLogger.init(new File("./config/logs.properties"));
			GlobalLang.load("english");

			System.out.println(EvolutionTree.load().toString(0));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public void stopMarkCount() {
		score = (long) ((System.currentTimeMillis() - startTime) * TIME_SCORE_MUL);
		score += Math.pow(achievements.size(), 3);
		score += 2 * ennemyKillCount;
		score += 6 * upgradeTreeCount;
	}

	public void startMarkCount() {
		startTime = System.currentTimeMillis();
	}

	public boolean canSelectUpgrade() {
		int needed = getUpgradePrice();
		return currentTreeNode != null && !currentTreeNode.isLeaf() && lipid >= needed && aminoAcid >= needed && glucose >= needed;
	}

	public int getUpgradePrice() {
		return (int) (Math.pow(2.05f, upgradeTreeCount) * 10);
	}

	public boolean selectUpgrade(int index) {
		if (index < 0) {
			return false;
		}

		if (currentTreeNode == null) {
			GlobalLogger.warning("Current tree node is null !");
			return false;
		}

		if (!canSelectUpgrade()) {
			return false;
		}

		if (index >= currentTreeNode.getChildren().size()) {
			return selectUpgrade(index - 1);
		}

		unlockAchievement(Achievements.UPGRADE_MK1);

		int price = getUpgradePrice();

		lipid -= price;
		aminoAcid -= price;
		glucose -= price;

		currentTreeNode = getCurrentTreeNode().getChildren().get(index);
		GlobalLogger.info("Unlocked: " + currentTreeNode);

		if (currentTreeNode.isLeaf()) {
			unlockAchievement(Achievements.UPGRADE_LATEST);
		}

		upgradeTreeCount++;

		lipid = 9999;
		aminoAcid = 9999;
		glucose = 9999;
		
		switch (currentTreeNode.getType()) {
		case "damage":
			if (damage < 1) {
				damage = 1;
			} else {
				damage *= 2;
			}
			break;
		case "photosynthesis":
			photosynthesis += 1;
			break;
		case "add_max_health":
			maxHealth += upgradeTreeCount;
			((UISceneGameOverlay) GlobalUtils.INSTANCE.uiScene.getState()).startHealthRestoreAccepted();
			break;
		case "double_max_health":
			maxHealth *= 2;
			((UISceneGameOverlay) GlobalUtils.INSTANCE.uiScene.getState()).startHealthRestoreAccepted();
			break;
		case "speed":
			speed += upgradeTreeCount;
			break;
		case "toxin_resistance":
			toxinResistant = true;
			break;
		case "predator_repulsion":
			predatorRepulsion = true;
			break;
		case "poison_trail":
			unlockAchievement(Achievements.UPGRADE_OFFENSIVE);
			poisonTrail = true;
			break;
		case "poison_damage":
			unlockAchievement(Achievements.UPGRADE_OFFENSIVE);
			poisonDamage = true;
			break;
		}

		Optional.ofNullable(GlobalUtils.INSTANCE.worldScene.getWorld()).ifPresent((w) -> {
			PlayerCellMaterial playerMaterial = w.getPlayer().getPlayerMaterial(w.getCache());
			playerMaterial.setDamage(PCUtils.clamp(0, 3, damage));
			playerMaterial.setPhoto(PCUtils.clamp(0, 3, photosynthesis));
			playerMaterial.setSpeed(PCUtils.clamp(0, 3, getSpeedTextureLevel()));
			playerMaterial.setHealth(PCUtils.clamp(0, 3, getHealthTextureLevel()));
		});

		return true;
	}

	public int getSpeedTextureLevel() {
		return speed - START_SPEED;
	}

	public int getHealthTextureLevel() {
		return (health - START_HEALTH) / 3;
	}

	public void incGlucose() {
		glucose++;
	}

	public EvolutionTreeNode getUpgrade(int index) {
		if(currentTreeNode.isLeaf()) {
			return null;
		}
		
		if (index >= currentTreeNode.getChildren().size()) {
			return getUpgrade(index - 1);
		}
		
		return currentTreeNode.getChildren().get(index);
	}

}
