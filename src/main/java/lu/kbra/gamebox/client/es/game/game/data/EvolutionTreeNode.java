package lu.kbra.gamebox.client.es.game.game.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import lu.pcy113.pclib.PCUtils;
import lu.pcy113.pclib.logger.GlobalLogger;

import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;

public class EvolutionTreeNode {

	public static final Map<String, Integer> ICON_IDS = new HashMap<String, Integer>(1) {
		{
			put("damage", 1);
			put("photosynthesis", 2);
			put("add_max_health", 3);
			put("speed", 4);
			put("double_max_health", 5);
			put("toxin_resistance", 6);
			put("poison_damage", 7);
			put("predator_repulsion", 8);
			put("poison_trail", 8);
			put("root", 3);
		}
	};

	private List<EvolutionTreeNode> parents = new ArrayList<EvolutionTreeNode>();
	private List<EvolutionTreeNode> children;

	private String id;
	private String type;
	private String title;
	private String description;

	private boolean checked = false;

	public EvolutionTreeNode(String id, String type) throws JSONException, IOException {
		this.id = id;
		this.type = type;

		GlobalLogger.info("Node path: " + "./resources/gd/majorTree/" + GlobalLang.getCURRENT_LANG().toLowerCase() + "/" + type + ".json");
		JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/majorTree/" + GlobalLang.getCURRENT_LANG().toLowerCase() + "/" + type + ".json"))));
		this.title = obj.optString("title", id + "/" + type);
		this.description = obj.optString("description", "no desc :/");
	}

	public void add(EvolutionTreeNode node) {
		if (children == null)
			children = new ArrayList<EvolutionTreeNode>();

		children.add(node);
		node.parents.add(this);
	}

	public void add(List<EvolutionTreeNode> ch) {
		if (children == null)
			children = new ArrayList<EvolutionTreeNode>();

		ch.forEach(c -> this.add(c));
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public boolean isLeaf() {
		return children == null || children.isEmpty();
	}

	public boolean isRoot() {
		return parents == null || parents.isEmpty();
	}

	public List<EvolutionTreeNode> getChildren() {
		return children;
	}

	@Override
	public String toString() {
		return id + "(type="+type+", name=" + title.replace("\n", "<br>") + ", desc=" + description + ")";
	}

	public String toString(int i) {
		String tabs = PCUtils.repeatString("  ", i);
		String content = toString();
		String ending = isLeaf() ? "" : children.stream().map(c -> c.toString(i + 1)).collect(Collectors.joining(",\n", "[\n", "\n" + tabs + "]"));
		return tabs + content + ending + "";
	}

	public int getIconId() {
		if (!ICON_IDS.containsKey(type)) {
			GlobalLogger.severe("Icon id not found for: " + type);
			return 0;
		}
		return ICON_IDS.get(type);
	}

}
