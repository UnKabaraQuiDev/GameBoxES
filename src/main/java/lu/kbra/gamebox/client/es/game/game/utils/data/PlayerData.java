package lu.kbra.gamebox.client.es.game.game.utils.data;

public class PlayerData {

	private int health = 2;
	private int speed = 1;

	public int getNextHealthPrice() {
		return 2*health;
	}
	
	public int getNextSpeedPrice() {
		return 2*speed;
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
