package lu.kbra.gamebox.client.es.game.game.data;

import java.io.IOException;

import org.json.JSONException;

import lu.kbra.gamebox.client.es.game.game.scenes.ui.UISceneMajorUpgradeTree;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalUtils;

public class PlayerData {

	private int health = 2, maxHealth = health;;
	private int speed = 1;

	private int glucose = 0;
	private int aminoAcid = 0;
	/**
	 * To upgrade the cell aka health, from plants or other dead cells
	 */
	private int lipid = 0;

	private EvolutionTree tree;
	private EvolutionTreeNode current;

	public PlayerData() {
		try {
			tree = EvolutionTree.load();
			current = tree;
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}
	}

	public void eatPlant() {
		int rand = (int) (Math.random() * 3);
		if (rand < 1) {
			glucose++;
		} else if (rand < 2) {
			aminoAcid++;
		} else if (rand < 3) {
			lipid++;
		}
		/**glucose += Math.random() < 0.4f ? 1 : 0;
		aminoAcid += Math.random() < 0.1f ? 1 : 0;
		lipid += Math.random() < 0.1f ? 1 : 0;*/
	}

	public void eatCell() {
		glucose += Math.random() < 0.5f ? 1 : 0;
		aminoAcid += Math.random() < 0.4f ? 1 : 0;
		lipid += Math.random() < 0.4f ? 1 : 0;
	}

	/**
	 * @return true if the player dies
	 */
	public boolean damage(int damage) {
		this.health -= damage;
		((UISceneMajorUpgradeTree) GlobalUtils.INSTANCE.uiScene.getState()).startHealthEmpty();
		return this.health <= 0;
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
		if(canRestoreHealth()) {
			lipid -= getNextRestoreHealthPrice();
			health++;
			return true;
		}
		return false;
	}

	public EvolutionTreeNode getCurrent() {
		return current;
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

}
