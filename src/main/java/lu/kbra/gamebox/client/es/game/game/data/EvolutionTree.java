package lu.kbra.gamebox.client.es.game.game.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.pcy113.pclib.logger.GlobalLogger;

/**
 * Random [+3;-3] for each upgrade price
 */
public class EvolutionTree extends EvolutionTreeNode {

	public EvolutionTree(String id) throws JSONException, IOException {
		super(id, "root");
	}

	public static EvolutionTree load() throws JSONException, IOException {
		JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/majorTree/data.json"))));

		EvolutionTree root = new EvolutionTree(obj.keys().next());

		loadInto(new ArrayList<EvolutionTreeNode>(), root, obj.getJSONObject(root.getId()));

		return root;
	}

	private static void loadInto(List<EvolutionTreeNode> nodes, EvolutionTreeNode parent, JSONObject obj) throws JSONException, IOException {
		for (String k : obj.keySet()) { // iterate over self-children
			GlobalLogger.info("Major Evolution Tree: Loading: " + k + " into " + parent.getId());

			if (nodes.stream().anyMatch(c -> c.getId().equals(k))) {
				parent.add(nodes.stream().filter(c -> c.getId().equals(k)).findFirst().get());
			} else {
				EvolutionTreeNode node = new EvolutionTreeNode(k, k.startsWith("random") ? getRandomType() : k);
				parent.add(node);
				nodes.add(node);

				if (obj.optJSONObject(k) != null) { // has children
					loadInto(nodes, node, obj.getJSONObject(k));
				} else { // is leaf parent
					loadInto(nodes, node, obj.getJSONArray(k));
				}
			}
		}
	}

	private static String getRandomType() {
		int rand = (int) (Math.random() * 4);
		switch (rand) {
		case 0:
			return "photosynthesis";
		case 1:
			return "damage";
		case 2:
			return "max_health";
		case 3:
			return "speed";
		}
		return null;
	}

	private static void loadInto(List<EvolutionTreeNode> nodes, EvolutionTreeNode parent, JSONArray obj) throws JSONException, IOException {
		for (int i = 0; i < obj.length(); i++) { // iterate over self-children
			String k = obj.getString(i);

			GlobalLogger.info("Major Evolution Tree: Loading leaf: " + k + " into " + parent.getId());

			if (nodes.stream().anyMatch(c -> c.getId().equals(k))) {
				parent.add(nodes.stream().filter(c -> c.getId().equals(k)).findFirst().get());
			} else {
				EvolutionTreeNode node = new EvolutionTreeNode(k, k.startsWith("random") ? getRandomType() : k);
				parent.add(node);
				nodes.add(node);
			}

		}
	}

}
