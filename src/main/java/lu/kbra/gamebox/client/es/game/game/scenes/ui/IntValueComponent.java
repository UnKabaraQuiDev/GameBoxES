package lu.kbra.gamebox.client.es.game.game.scenes.ui;

import org.joml.Math;

import lu.kbra.gamebox.client.es.engine.objs.entity.Component;

public class IntValueComponent extends Component {

	private int value;
	private boolean clamp, mod;
	private int clampBoundsMin, clampBoundsMax;
	
	public IntValueComponent(int value) {
		this.value = value;
	}
	
	public IntValueComponent(int val, int clampBoundsMin, int clampBoundsMax) {
		this.value = val;
		this.clampBoundsMin = clampBoundsMin;
		this.clampBoundsMax = clampBoundsMax;
		this.clamp = true;
	}
	
	public IntValueComponent(int val, int mod) {
		this.value = val;
		this.clampBoundsMax = mod;
		this.mod = true;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
		clamp();
		mod();
	}

	private void mod() {
		if(mod) {
			value = java.lang.Math.floorMod(value, clampBoundsMax);
			System.out.println("true mod: "+value+" "+clampBoundsMax);
		}
	}

	private void clamp() {
		if(clamp) {
			value = Math.clamp(clampBoundsMin, clampBoundsMax, value);
		}
	}

}
