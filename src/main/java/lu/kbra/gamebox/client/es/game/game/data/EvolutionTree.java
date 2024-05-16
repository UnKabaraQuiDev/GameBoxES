package lu.kbra.gamebox.client.es.game.game.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lu.pcy113.pclib.GlobalLogger;

/**
 * Random [+3;-3] for each upgrade price
 */
public class EvolutionTree extends EvolutionTreeNode {

	public EvolutionTree(String id) throws JSONException, IOException {
		super(id);
	}

	public static EvolutionTree load() throws JSONException, IOException {
		JSONObject obj = new JSONObject(new String(Files.readAllBytes(Paths.get("./resources/gd/majorTree/data.json"))));

		EvolutionTree root = new EvolutionTree(obj.keys().next());

		loadInto(root, obj.getJSONObject(root.getId()));

		return root;
	}

	private static void loadInto(EvolutionTreeNode parent, JSONObject obj) throws JSONException, IOException {
		for (String k : obj.keySet()) { // iterate over self-children
			GlobalLogger.info("Major Evolution Tree: Loading: " + k + " into " + parent.getId());

			EvolutionTreeNode node = new EvolutionTreeNode(k);
			parent.add(node);

			if (obj.optJSONObject(k) != null) { // has children
				loadInto(node, obj.getJSONObject(k));
			} else { // is leaf parent
				loadInto(node, obj.getJSONArray(k));
			}
		}
	}

	private static void loadInto(EvolutionTreeNode parent, JSONArray obj) throws JSONException, IOException {
		for (int i = 0; i < obj.length(); i++) { // iterate over self-children
			String k = obj.getString(i);
			
			GlobalLogger.info("Major Evolution Tree: Loading leaf: " + k + " into " + parent.getId());

			EvolutionTreeNode node = new EvolutionTreeNode(k);
			parent.add(node);
		}
	}

}
