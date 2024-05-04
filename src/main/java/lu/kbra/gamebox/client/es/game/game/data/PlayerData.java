package lu.kbra.gamebox.client.es.game.game.data;

import java.io.IOException;

import org.json.JSONException;

public class PlayerData {

	private int health = 2;
	private int speed = 1;

	private int glucose = 0;
	private int aminoAcid = 0;
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

	public void setGlucose(int glucose) {
		this.glucose = glucose;
	}

	public void setAminoAcid(int aminoAcid) {
		this.aminoAcid = aminoAcid;
	}

	public void setLipid(int lipid) {
		this.lipid = lipid;
	}

	public int getNextHealthPrice() {
		return 2 * health;
	}

	public int getNextSpeedPrice() {
		return 2 * speed;
	}

	public boolean upgradeHealth() {
		if (canUpgradeHealth()) {
			health++;
			return true;
		} else {
			return false;
		}
	}

	public boolean canUpgradeHealth() {
		return lipid >= getNextHealthPrice();
	}

	public boolean upgradeSpeed() {
		if (canUpgradeSpeed()) {
			speed++;
			return true;
		} else {
			return false;
		}
	}

	public boolean canUpgradeSpeed() {
		return glucose >= getNextSpeedPrice();
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

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
