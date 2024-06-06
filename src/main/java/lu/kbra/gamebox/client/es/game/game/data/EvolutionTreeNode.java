package lu.kbra.gamebox.client.es.game.game.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;

import lu.kbra.gamebox.client.es.engine.utils.PDRUtils;
import lu.kbra.gamebox.client.es.game.game.utils.global.GlobalLang;

public class EvolutionTreeNode {

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
		
		System.out.println("path: "+"./resources/gd/majorTree/" + GlobalLang.getCURRENT_LANG().toLowerCase() + "/" + type + ".json");
		JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/majorTree/" + GlobalLang.getCURRENT_LANG().toLowerCase() + "/" + type + ".json"))));
		this.title = obj.optString("title", id + "/" + type);
		this.description = obj.optString("desc", "no desc :/");
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

	public boolean isLeaf() {
		return children == null || children.isEmpty();
	}
	
	public boolean isRoot() {
		return parents == null || parents.isEmpty();
	}

	public List<EvolutionTreeNode> getChildren() {
		return children;
	}

	public String toString(int i) {
		String tabs = PDRUtils.repeatString("  ", i);
		String content = id + "(name=" + title.replace("\n", "<br>") + ", desc=" + description + ")";
		String ending = isLeaf() ? "" : children.stream().map(c -> c.toString(i + 1)).collect(Collectors.joining(",\n", "[\n", "\n" + tabs + "]"));
		return tabs + content + ending + "";
	}

}
