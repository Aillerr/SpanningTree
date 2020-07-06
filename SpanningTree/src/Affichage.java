

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import javafx.util.Pair;

public class Affichage {

	private int nbNodes;
	private HashMap<Pair<Integer, Integer>, Integer> myGraph;

	public Affichage(int n, HashMap<Pair<Integer, Integer>, Integer> data) {
		this.nbNodes = n;
		this.myGraph = data;
	}

	public void init() {
		Graph graph = new SingleGraph("Graphe !");

		graph.addAttribute("ui.stylesheet", "url('file:css/styles.css')");

		initNodes(nbNodes, graph);
		initEdges(myGraph, graph);

		graph.display();
	}

	public void initNodes(int nbNodes, Graph graph) {
		for(int i=1; i<=nbNodes; i++) {
			graph.addNode(Integer.toString(i));
		}
		for (Node nd : graph) {
			nd.addAttribute("ui.label", nd.getId());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initEdges(HashMap<Pair<Integer, Integer>, Integer> myGraph, Graph graph) {
		Iterator it = myGraph.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry entryPair = (Map.Entry) it.next();
			String edgeName = entryPair.getKey().toString();
			Pair<Integer, Integer> pair = (Pair<Integer, Integer>) entryPair.getKey();
			Node n1 = graph.getNode(pair.getKey()-1);
			Node n2 = graph.getNode(pair.getValue()-1);
			try {
			graph.addEdge(edgeName, n1,n2).addAttribute("ui.label", entryPair.getValue());
			}
			catch(NullPointerException e) {
				System.out.println("Echec de l'ajout de l'edge : " + pair);
			}
			it.remove();
		}
		
	}

}
