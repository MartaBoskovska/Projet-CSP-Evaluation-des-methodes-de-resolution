import java.io.BufferedReader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.extension.Tuples;
import org.chocosolver.solver.variables.IntVar;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import java.util.Arrays;
import java.util.stream.*;

public class Expe2 {

	private static Model lireReseau(BufferedReader in) throws Exception {
		Model model = new Model("Expe2");

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

		
		//int[] listeNbContraint = {220, 230, 240, 250, 260, 270, 280, 290, 300, 310, 320, 330};
		int[] listeNbTuples = {211,208,205,202,199,196,193,190,187,184,181,178};
		int nbVar = 30;
		int nbRes = 10;
		int tailleDom = 17;
		int nbContraint = 240;

		/* Generation d'un fichier csv */
		String outputPath = "output1.csv";
		FileWriter fileWriter = new FileWriter(outputPath);
		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

		/* Tableau avec le pourcentage de reseau satisfaits de chaque essaie */
		String[] liste = new String[3];
		float pourcentageResSol = 0;

		long averageTime = 0;
		long averageDepth = 0;
		
		/*Liste de differentes valeurs de temps de calcul*/
		long[] listeTime = new long[nbRes];
		long[] listeTailleArbre = new long[nbRes];
		
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
				boolean solved;
				
				ThreadMXBean thread = ManagementFactory.getThreadMXBean();
				
				/*Measurement du temps de calcul*/
				long startTime = System.nanoTime();
				long startCpuTime = thread.getCurrentThreadCpuTime();
				long startUserTime = thread.getCurrentThreadUserTime();
				
				/*Le calcul*/
				solved = model.getSolver().solve();
				
				long userTime = thread.getCurrentThreadUserTime() - startUserTime;
				long cpuTime = thread.getCurrentThreadCpuTime() - startCpuTime;
				long sysTime = cpuTime - userTime;
				long realTime = System.nanoTime() - startTime;
				
				/*Calcul de la taille de l'arbre de recherche*/
				
				long tailleArbre = model.getSolver().getMaxDepth();
				
				listeTailleArbre[nb-1] = tailleArbre;
				listeTime[nb-1] = cpuTime;
				
				if (solved) {
					nbReseauxSol++;

				} else if (model.getSolver().isStopCriterionMet()) {
					System.out.println(
							"The solver could not find a solution nor prove that none exists in the given limits");
				} else {
					System.out.println("The solver has proved the problem has no solution");
				}
				
				
			}
			
			/*Calcul de la durete*/
			float durete = ((float)(tailleDom*tailleDom) - i) / (float)(tailleDom*tailleDom);
			durete *= 100;
			
			/*Calcul de la densite*/
			//float densite = ((float)(2*i)) / (float)((nbVar*nbVar)-nbVar);
			//densite *= 100;
			
			
		
			/*enlever les plus petites et plus grandes valeur*/
			Arrays.sort(listeTime);
			long[] modifiedlisteTime = Arrays.copyOfRange(listeTime, 1, listeTime.length-1);
			

			
			
			pourcentageResSol =  ((float) nbReseauxSol / nbRes) * 100;
			
			averageTime = Arrays.stream(modifiedlisteTime).sum() / (nbRes-2);
			averageTime /= 1000000;
			
			averageDepth = Arrays.stream(listeTailleArbre).sum() / (nbRes);
			
			/*Affichage de la pourcentage de reseaux qui ont une solution*/
			System.out.println("Le nombre de reseaux qui ont une solution " + nbReseauxSol);
			System.out.println(
					"Pourcentage de reseaux qui ont une solution " + pourcentageResSol + "%");
			
			
			/* Ecriture dans un fichier CSV */
			liste[0] = Float.toString(durete);
			liste[1] = Float.toString(pourcentageResSol);
			//liste[2] = Long.toString(averageTime);
			System.out.println(averageDepth);
			liste[2] = Long.toString(averageDepth);
			
			String dataLine =  String.join(",", liste);
			bufferedWriter.write(dataLine);
			bufferedWriter.newLine();	
		}
		bufferedWriter.close();

		System.out.println("Le fichier CSV a ete cree avec success..");

	}

}
