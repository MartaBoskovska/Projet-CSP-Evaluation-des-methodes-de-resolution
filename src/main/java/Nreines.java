import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class Nreines {
	
	public static void main(String[] args) {
		
		//Nombre de reines
		int n = 16;
		
		// Création du modele
		Model model = new Model("NReines");
		
		// Création des variables
		IntVar [] t = model.intVarArray("x",n,0,n-1);
		
		
		// Création des contraintes
		model.allDifferent(t).post();
		
		for (int i = 0; i < n; i++) {
		    for (int j = i + 1; j < n; j++) {
		        
		        model.arithm(t[i], "!=", t[j], "-", j - i).post();
		        model.arithm(t[i], "!=", t[j], "+", j - i).post();
		    }
		}
		
        // Affichage du réseau de contraintes créé
        System.out.println("*** Réseau Initial ***");
        System.out.println(model);
        

        // Calcul de la première solution
        if(model.getSolver().solve()) {
        	System.out.println("\n\n*** Première solution ***");        
        	System.out.println(model);
        }
		
    	// Calcul de toutes les solutions
    	System.out.println("\n\n*** Autres solutions ***");        
        while(model.getSolver().solve()) {    	
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n");
	    }
        System.out.println("\n\n*** END ***");  
	}
}
