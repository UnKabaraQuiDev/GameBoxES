package lu.kbra.gamebox.client.es.game.game.data;

import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;

public enum Achievements {

	KILL_AN_ENNEMY("ennemy_kill"),
	EAT_A_PLANT("eat_a_plant"),
	OVERLOADED("too_much_materials"),
	UPGRADE_MK1("upgrade.mk1"),
	UPGRADE_OFFENSIVE("upgrade.offensive"),
	UPGRADE_LATEST("upgrade.latest"),
	ENNEMY_DAMAGE("get_damaged_by_ennemy"),
	FOG_DAMAGE("get_damaged_by_fog"),
	HEAVY_POCKETS("many_materials");

	private String key;

	private Achievements(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String getTotalKey() {
		return "achievements." + key;
	}

	public String getTranslationTitle() {
		return GlobalLang.get(getTotalKey());
	}
	
	/*public String getTranslationDescription() {
		return GlobalLang.get(getTotalKey()+".desc");
	}*/

}
