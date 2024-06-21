package lu.kbra.gamebox.client.es.game.game.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.game.game.scenes.ui.UISceneGameOverlay;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class PlayerData {

	private static final float TIME_SCORE_MUL = 0.001f;

	private int health = 2, maxHealth = health;
	private int damage = 1;
	private int photosynthesis = 0;
	private int speed = 1;
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

		/**
		 * glucose += Math.random() < 0.4f ? 1 : 0; aminoAcid += Math.random() < 0.1f ? 1 : 0; lipid += Math.random() < 0.1f ? 1 : 0;
		 */
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

	public boolean isPoisonTrail() {
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
		int needed = (int) (Math.pow(2.05f, upgradeTreeCount) * 10);
		return lipid >= needed && aminoAcid >= needed && glucose >= needed;
	}

	public boolean selectUpgrade(int index) {
		currentTreeNode = getCurrentTreeNode().getChildren().get(index);

		if (!canRestoreHealth())
			return false;

		upgradeTreeCount++;

		switch (currentTreeNode.getId()) {
		case "damage":
			damage *= 2;
			break;
		case "photosynthesis":
			photosynthesis += 1;
			break;
		case "add_max_health":
			maxHealth += upgradeTreeCount;
			// ((UISceneGameOverlay) GlobalUtils.INSTANCE.uiScene.getState()).startHealthRestoreAccepted();
			break;
		case "double_max_health":
			maxHealth *= upgradeTreeCount;
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
			poisonTrail = true;
			break;
		case "poison_damage":
			poisonDamage = true;
			break;
		}

		return true;
	}

}
