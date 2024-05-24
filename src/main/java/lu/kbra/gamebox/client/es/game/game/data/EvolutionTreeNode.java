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

	private List<EvolutionTreeNode> children;

	private String id;
	private String title;
	private String description;

	private boolean checked = false;

	public EvolutionTreeNode(String id) throws JSONException, IOException {
		this.id = id;

		JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/majorTree/" + GlobalLang.getCURRENT_LANG().toLowerCase() + "/" + id + ".json"))));
		this.title = obj.optString("title", id);
		this.description = obj.optString("desc", "no desc :/");
	}

	public void add(EvolutionTreeNode node) {
		if (children == null)
			children = new ArrayList<EvolutionTreeNode>();

		children.add(node);
	}

	public void add(List<EvolutionTreeNode> ch) {
		if (children == null)
			children = new ArrayList<EvolutionTreeNode>();

		children.addAll(ch);
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
