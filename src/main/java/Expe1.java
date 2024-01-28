import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

public class Expe1 {

	private static Model lireReseau(BufferedReader in) throws Exception {
		Model model = new Model("Expe1");

		int nbVariables = Integer.parseInt(in.readLine()); // le nombre de variables
		int tailleDom = Integer.parseInt(in.readLine()); // la valeur max des domaines
		IntVar[] var = model.intVarArray("x", nbVariables, 0, tailleDom - 1);
		int nbConstraints = Integer.parseInt(in.readLine()); // le nombre de contraintes binaires
		for (int k = 1; k <= nbConstraints; k++) {
			String chaine[] = in.readLine().split(";");
			IntVar portee[] = new IntVar[] { var[Integer.parseInt(chaine[0])], var[Integer.parseInt(chaine[1])] };
			int nbTuples = Integer.parseInt(in.readLine()); // le nombre de tuples
			Tuples tuples = new Tuples(new int[][] {}, true);
			for (int nb = 1; nb <= nbTuples; nb++) {
				chaine = in.readLine().split(";");
				int t[] = new int[] { Integer.parseInt(chaine[0]), Integer.parseInt(chaine[1]) };
				tuples.add(t);
			}
			model.table(portee, tuples).post();
		}
		in.readLine();
		return model;
	}

	public static void main(String[] args) throws Exception {

		int[] listeNbTuples = {211, 208, 205, 202, 199, 196, 193, 190, 187, 184, 181, 178};
		//int[] listeNbContraint = {220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330};
		
		/* Variable pour spécifier le nombre de réseaux pour un benchmark */
		int nbRes = 10;

		/* Generation d'un fichier csv */
		String outputPath = "output3_a.csv";
		FileWriter fileWriter = new FileWriter(outputPath);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		/* Tableau avec le pourcentage de reseau satisfaits de chaque essaie */
		String[] pourcentageResSatisf = new String[12];

		int cpt = 0;
		for (int i : listeNbTuples) {

			int nbReseauxSol = 0;

			String ficName = "csp" + i + ".txt";

			BufferedReader readFile = new BufferedReader(new FileReader(ficName));

			for (int nb = 1; nb <= nbRes; nb++) {
				Model model = lireReseau(readFile);
				if (model == null) {
					System.out.println("Probl�me de lecture de fichier !\n");
					return;
				}

				// Limiter le temps de r�solution � 10 secondes
				model.getSolver().limitTime("10s");

				// Calcul de la premi�re solution
				if (model.getSolver().solve()) {
					nbReseauxSol++;

				} else if (model.getSolver().isStopCriterionMet()) {
					System.out.println(
							"The solver could not find a solution nor prove that none exists in the given limits");
				} else {
					System.out.println("The solver has proved the problem has no solution");
				}
			}

			System.out.println("Le nombre de r�seaux qui ont une solution " + nbReseauxSol);
			System.out.println(
					"Pourcentage de r�seaux qui ont une solution " + ((float) nbReseauxSol / nbRes) * 100 + "%");
			pourcentageResSatisf[cpt] = Float.toString(((float) nbReseauxSol / nbRes) * 100);
			cpt++;

		}

		/* Ecriture dans un fichier CSV */
		String dataLine = String.join(",", pourcentageResSatisf);
		bufferedWriter.write(dataLine);
		bufferedWriter.newLine();
		bufferedWriter.close();

		System.out.println("Le fichier CSV a ete cree avec success..");

	}

}
