import java.io.File;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		//Execution sur tous les graphes
/*
		STree st = new STree();
		File f = new File("graphes");
		String[] fileList = f.list();
		for (String str : fileList) {
			System.out.println(str);
			if (str.endsWith(".mst"))
				st.exec(str.split("[\\Q.\\E]")[0], 3);

		}
*/
		
		//Execution sur un graphe voulu
		System.out.println("Quel fichier voulez vous tester ?");
		Scanner sc = new Scanner(System.in);

		String fichier = sc.nextLine();

		System.out.println("Quelle est la valeur de d voulue ?");
		String d = sc.nextLine();

		STree st = new STree();
		st.exec(fichier, Integer.parseInt(d));

		sc.close();
		
	}

}
