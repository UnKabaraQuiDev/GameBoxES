package lu.kbra.gamebox.client.es.engine.utils.consts;

import org.joml.Vector2f;

public enum Direction {

	NORTH(new Vector2f(0, 1), 0), EST(new Vector2f(1, 0), 1), SOUTH(new Vector2f(0, -1), 2), WEST(new Vector2f(-1, 0), 3),
	NONE(new Vector2f(0, 0), -1);

	private Vector2f dir;
	private int index;

	Direction(Vector2f v, int index) {
		this.dir = v;
		this.index = index;
	}

	public Vector2f getDirection() {
		return dir;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static Direction getByIndex(int i) {
		for(Direction d : values()) {
			if(d.getIndex() == i)
				return d;
		}
		return NONE;
	}

}
