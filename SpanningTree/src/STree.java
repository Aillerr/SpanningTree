import static java.util.stream.Collectors.toMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import javafx.util.Pair;

public class STree {

	private HashMap<Pair<Integer, Integer>, Integer> data;
	private HashMap<Pair<Integer, Integer>, Integer> tree;
	private BufferedReader bufferedR;
	private int nbSommets;
	@SuppressWarnings("unused")
	private int nbArcs;

	public STree() {

	}

	public void exec(String file, int d) {

		long start;
		long tpsK1, tpsK2, tpsPr, tpsMST;
		int pdsK1, pdsK2, pdsPr, pdsDMST;
		
		readFile("graphes/" + file + ".mst");
		//System.out.println("Aperçu du graphe : ");
		//System.out.println(data);
		
		//KRUKSAL 1
		start = System.nanoTime()/1000;
		tree = Kruskal1(sortMapCroissant(data), nbSommets);
		tpsK1 = System.nanoTime()/1000 - start;
		pdsK1=sommePoids(tree, file, "Kruskal1");
		Affichage affKruskal1 = new Affichage(nbSommets, tree);
		affKruskal1.init();
		printTime("Kruskal1", tpsK1);
		
		
		//KRUSKAL 2
		start = System.nanoTime()/1000;
		tree = Kruskal2(sortMapDecroissant(data), nbSommets);
		tpsK2 = System.nanoTime()/1000 - start;
		pdsK2=sommePoids(tree, file, "Kruskal2");
		Affichage affKruskal2 = new Affichage(nbSommets, tree);
		affKruskal2.init();
		printTime("Kruskal2", tpsK2);
		
		
		//PRIM
		start = System.nanoTime()/1000;
		tree = Prim(sortMapCroissant(data), nbSommets);
		tpsPr =System.nanoTime()/1000- start;
		pdsPr=sommePoids(tree, file, "Prim");
		Affichage affPrism = new Affichage(nbSommets, tree);
		affPrism.init();
		printTime("Prim", tpsPr);
		
		
		//D-MST
		start = System.nanoTime()/1000;
		tree = dMST(sortMapCroissant(data), d, nbSommets);
		tpsMST = System.nanoTime()/1000 - start;
		pdsDMST=sommePoids(tree, file, "d-MST");
		Affichage affDMST = new Affichage(nbSommets, tree);
		affDMST.init();
		printTime("d-MST", tpsMST);
		//write(file, pdsK1, tpsK1, pdsK2, tpsK2, pdsPr, tpsPr, pdsDMST, tpsMST);
		/*
		System.out.println("=======================");
		
		start = System.nanoTime()/1000;
		tree = dMST(sortMapCroissant(data), 5, nbSommets);
		long tpsMST1 = System.nanoTime()/1000 - start;
		int pdsDMST1=sommePoids(tree, file, "5-MST");
		Affichage affDMST1 = new Affichage(nbSommets, tree);
		affDMST1.init();
		printTime("5-MST", tpsMST1);

		start = System.nanoTime()/1000;
		tree = dMST(sortMapCroissant(data), 7, nbSommets);
		long tpsMST2 = System.nanoTime()/1000 - start;
		int pdsDMST2=sommePoids(tree, file, "7-MST");
		Affichage affDMST2 = new Affichage(nbSommets, tree);
		affDMST2.init();
		printTime("7-MST", tpsMST2);
		
		start = System.nanoTime()/1000;
		tree = dMST(sortMapCroissant(data), 9, nbSommets);
		long tpsMST3 = System.nanoTime()/1000 - start;
		int pdsDMST3=sommePoids(tree, file, "9-MST");
		Affichage affDMST3 = new Affichage(nbSommets, tree);
		affDMST3.init();
		printTime("9-MST", tpsMST3);
		
		start = System.nanoTime()/1000;
		tree = dMST(sortMapCroissant(data), 11, nbSommets);
		long tpsMST4 = System.nanoTime()/1000 - start;
		int pdsDMST4=sommePoids(tree, file, "11-MST");
		Affichage affDMST4 = new Affichage(nbSommets, tree);
		affDMST4.init();
		printTime("11-MST", tpsMST4);
		//writeMST(file, pdsDMST1, tpsMST1, pdsDMST2, tpsMST2, pdsDMST3, tpsMST3, pdsDMST4, tpsMST4);
		System.out.println("=======================");
		*/
	}

	public void printTime(String method, long t) {
		System.out.println("Temps pour la méthode " + method +" : " + t);
	}
	
	public int sommePoids(HashMap<Pair<Integer, Integer>, Integer> data, String file, String method) {
		int tmp = 0;
		for (Pair<Integer, Integer> pair : data.keySet()) {
			tmp += data.get(pair);

		}
		System.out.print("Poids total de l'arbre en utilisant " + method + " sur " + file + " : ");
		System.err.println(tmp);
		return tmp;
	}

	public void readFile(String file) {
		try {
			bufferedR = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
			String line;
			String words[];
			data = new HashMap<Pair<Integer, Integer>, Integer>();
			while ((line = bufferedR.readLine()) != null) {

				words = line.split(" ");

				if (words[0].equals("p")) {

					nbSommets = Integer.parseInt(words[1]);
					nbArcs = Integer.parseInt(words[2]);
				}
				if (words[0].equals("e")) {

					int sommet1 = Integer.parseInt(words[1]);
					int sommet2 = Integer.parseInt(words[2]);
					int poids = Integer.parseInt(words[3]);

					Pair<Integer, Integer> pair = new Pair<Integer, Integer>(sommet1, sommet2);

					data.put(pair, poids);
				}
			}
			bufferedR.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<Pair<Integer, Integer>, Integer> sortMapCroissant(HashMap<Pair<Integer, Integer>, Integer> data) {
		//System.out.println(data);
		HashMap<Pair<Integer, Integer>, Integer> sorted = data.entrySet().stream().sorted(Map.Entry.comparingByValue())
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		//System.out.println(sorted);
		return sorted;
	}

	public HashMap<Pair<Integer, Integer>, Integer> sortMapDecroissant(HashMap<Pair<Integer, Integer>, Integer> data) {
		HashMap<Pair<Integer, Integer>, Integer> sorted = data.entrySet().stream()
				.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		// System.out.println(sorted);
		return sorted;

	}

	public Boolean testCycle(HashMap<Pair<Integer, Integer>, Integer> data, int n, int starter) {
		HashMap<Integer, Integer> niveaux = new HashMap<Integer, Integer>();
		LinkedList<Integer> list = new LinkedList<Integer>();

		for (int i = 1; i <= n; i++) {
			niveaux.put(i, -1);
		}
		niveaux.replace(starter, 0);
		list.push(starter);
		while (!list.isEmpty()) {
			int a = list.pop();

			for (Pair<Integer, Integer> pair : data.keySet()) {

				if (pair.getKey() == a) {
					int b = pair.getValue();
					if (niveaux.get(b) == -1) {
						niveaux.replace(b, niveaux.get(a) + 1);
						list.push(b);
					} else {
						if (niveaux.get(b) >= niveaux.get(a))
							return true;
					}
				} else {
					if (pair.getValue() == a) {
						int b = pair.getKey();
						if (niveaux.get(b) == -1) {
							niveaux.replace(b, niveaux.get(a) + 1);
							list.push(b);
						} else {
							if (niveaux.get(b) >= niveaux.get(a))
								return true;
						}
					}
				}

			}

			/*
			 * catch(NullPointerException e) {
			 * 
			 * }
			 */
		}

		return false;
	}

	public HashMap<Pair<Integer, Integer>, Integer> Kruskal1(HashMap<Pair<Integer, Integer>, Integer> data, int n) {

		HashMap<Pair<Integer, Integer>, Integer> tmp = new HashMap<Pair<Integer, Integer>, Integer>();

		for (Pair<Integer, Integer> pair : data.keySet()) {
			// System.out.println(pair);
			if (tmp.size() >= n - 1) {
				// System.out.println(tmp + " size : " + tmp.size());

				return tmp;
			}
			tmp.put(pair, data.get(pair));
			if (testCycle(tmp, n, pair.getKey()))
				tmp.remove(pair);

		}

		return tmp;

	}

	public Boolean testConnexe(HashMap<Pair<Integer, Integer>, Integer> data, int n, int starter) {

		LinkedList<Integer> marque = new LinkedList<Integer>();
		LinkedList<Integer> list = new LinkedList<Integer>();

		int cptr = 1;
		marque.add(cptr);
		list.push(cptr);
		while (!list.isEmpty()) {
			int tmp = list.getFirst();
			for (Pair<Integer, Integer> pair : data.keySet()) {
				if (pair.getKey() == tmp && !(marque.contains(pair.getValue()))) {
					marque.add(pair.getValue());
					list.push(pair.getValue());
					cptr++;

				} else {
					if (pair.getValue() == tmp && !(marque.contains(pair.getKey()))) {
						marque.add(pair.getKey());
						list.push(pair.getKey());
						cptr++;

					}
				}
			}
			list.remove((Object) tmp);

		}
		// System.out.println(cptr);
		return (n == cptr);
	}

	@SuppressWarnings("unchecked")
	public HashMap<Pair<Integer, Integer>, Integer> Kruskal2(HashMap<Pair<Integer, Integer>, Integer> data, int n) {
		HashMap<Pair<Integer, Integer>, Integer> tmp = new HashMap<Pair<Integer, Integer>, Integer>();
		tmp = (HashMap<Pair<Integer, Integer>, Integer>) data.clone();

		for (Pair<Integer, Integer> pair : data.keySet()) {
			// System.out.println(pair);
			// System.out.println(/*tmp + */" size : " + tmp.size());
			if (tmp.size() < n) {

				return tmp;
			}

			tmp.remove(pair);
			if (!testConnexe(tmp, n, pair.getKey()))
				tmp.put(pair, data.get(pair));

		}

		return tmp;

	}

	public HashMap<Pair<Integer, Integer>, Integer> Prim(HashMap<Pair<Integer, Integer>, Integer> data, int n) {
	
		HashMap<Pair<Integer, Integer>, Integer> tmp = new HashMap<Pair<Integer, Integer>, Integer>();
		Pair<Integer, Integer> pair;
		int random = (int) (1 + (Math.random() * (n - 1)));
		LinkedList<Integer> list = new LinkedList<Integer>();
		list.add(random);

		while (n != list.size()) {

			pair = bestPair(data, list);
			tmp.put(pair, data.get(pair));
			if (list.contains(pair.getKey())) {
				list.add(pair.getValue());
			} else
				list.add(pair.getKey());

		}

		return tmp;
	}

	public Pair<Integer, Integer> bestPair(HashMap<Pair<Integer, Integer>, Integer> data, LinkedList<Integer> list) {

		for (Pair<Integer, Integer> pair : data.keySet()) {
			int p1 = pair.getKey();
			int p2 = pair.getValue();
			if ((list.contains(p1) && !(list.contains(p2))) || (list.contains(p2) && !(list.contains(p1)))) {
				return pair;

			}

		}
		return null;

	}

	public HashMap<Pair<Integer, Integer>, Integer> dMST(HashMap<Pair<Integer, Integer>, Integer> data, int d, int n) {
		HashMap<Integer, Integer> nodeNumber = new HashMap<Integer, Integer>();
		HashMap<Pair<Integer, Integer>, Integer> tmp = new HashMap<Pair<Integer, Integer>, Integer>();

		for (int i = 1; i <= n; i++) {
			nodeNumber.put(i, 0);
		}
		//System.out.println(nodeNumber);
		for (Pair<Integer, Integer> pair : data.keySet()) {
			// System.out.println(pair);
			if (tmp.size() >= n - 1) {
				return tmp;
			}
			tmp.put(pair, data.get(pair));
			
			nodeNumber.replace(pair.getKey(), nodeNumber.get(pair.getKey())+1);
			nodeNumber.replace(pair.getValue(), nodeNumber.get(pair.getValue())+1);

			if (testCycle(tmp, n, pair.getKey())
					|| (nodeNumber.get(pair.getKey()) > d) || (nodeNumber.get(pair.getValue()) > d)) {
				tmp.remove(pair);
				nodeNumber.replace(pair.getKey(), nodeNumber.get(pair.getKey())-1);
				nodeNumber.replace(pair.getValue(), nodeNumber.get(pair.getValue())-1);

			}
				

		}

		return tmp;
	}
	

	
	
	public void write(String file, int kr1, long tk1, int kr2, long tk2, int pr, long tpr, int dmst, long tmst) {
		final String filename = "res/res.txt";
		try(FileWriter fw = new FileWriter(filename, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(file +","+kr1+","+tk1+","+kr2+","+tk2+","+pr+","+tpr+","+dmst+","+tmst);

			} catch (IOException e) {
			    e.printStackTrace();
			}
	}
	
	public void writeMST(String file, int mst1, long t1, int mst2, long t2, int mst3, long t3, int mst4, long t4) {
		final String filename = "res/res-dmst.txt";
		try(FileWriter fw = new FileWriter(filename, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    out.println(file +","+mst1+","+t1+","+mst2+","+t2+","+mst3+","+t3+","+mst4+","+t4);

			} catch (IOException e) {
			    e.printStackTrace();
			}
	}

}
