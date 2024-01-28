import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

public class ZebreIntension {
	
	public static void main(String[] args) {
	

		// Création du modele
		Model model = new Model("Zebre");
		
		
		// Création des variables
		IntVar blu = model.intVar("Blue", 1, 5);	// blu est une variable entière dont le nom est "Blue" est le domaine [1,5]
		IntVar gre = model.intVar("Green", 1, 5); 
		IntVar ivo = model.intVar("Ivory", 1, 5);         
		IntVar red = model.intVar("Red", 1, 5);         
		IntVar yel = model.intVar("Yellow", 1, 5);   
		
		IntVar eng = model.intVar("English", 1, 5);         
		IntVar jap = model.intVar("Japanese", 1, 5);         
		IntVar nor = model.intVar("Norwegian", 1, 5);         
		IntVar spa = model.intVar("Spanish", 1, 5);         
		IntVar ukr = model.intVar("Ukrainian", 1, 5);         
		
		IntVar cof = model.intVar("Coffee", 1, 5);         
		IntVar mil = model.intVar("Milk", 1, 5);         
		IntVar ora = model.intVar("Orange Juice", 1, 5);         
		IntVar tea = model.intVar("Tea", 1, 5);         
		IntVar wat = model.intVar("Water", 1, 5);         
		
	    IntVar dog = model.intVar("Dog", 1, 5);         
	    IntVar fox = model.intVar("Fox", 1, 5);         
	    IntVar hor = model.intVar("Horse", 1, 5);         
	    IntVar sna = model.intVar("Snail", 1, 5);         
	    IntVar zeb = model.intVar("Zebra", 1, 5);         
	    
	    IntVar che = model.intVar("Chesterfield", 1, 5);         
	    IntVar koo = model.intVar("Kool", 1, 5);         
	    IntVar luc = model.intVar("Lucky Strike", 1, 5);         
	    IntVar old = model.intVar("Old Gold", 1, 5);         
	    IntVar par = model.intVar("Parliament", 1, 5);         

	    
	    // Création des contraintes
	    
	    model.allDifferent(blu, gre, ivo, red, yel).post();
	    model.allDifferent(eng, jap, nor, spa, ukr).post();
	    model.allDifferent(cof, mil, ora, tea, wat).post();
	    model.allDifferent(dog, fox, hor, sna, zeb).post();
	    model.allDifferent(che, koo, luc, old, par).post();
	    
        model.arithm(eng, "=", red).post(); // 2. L'Anglais vit dans la maison rouge
        model.arithm(spa, "=", dog).post();   // 3. L'Espagnol a un chien
        model.arithm(cof, "=", gre).post();   // 4. On boit du café dans la maison verte
        model.arithm(ukr, "=", tea).post();   // 5. On boit du thé en Ukraine
        model.arithm(ivo, "-", gre, "=", 1).post(); // 6. Le propriétaire de l'escargot vit à côté de celui qui boit du café
        model.arithm(old, "=", sna).post();   // 7. Le vieil homme fume les Old Gold
        model.arithm(koo, "=", yel).post();   // 8. Les Kools sont fumés dans la maison jaune
        model.arithm(mil, "=", 3).post();     // 9. On boit du lait dans la troisième maison
        model.arithm(nor, "=", 1).post();     // 10. Le Norvégien vit dans la première maison
        model.arithm(che, "-", fox, "=", -1).post(); // 11. L'homme qui fume les Chesterfields vit à côté de l'homme qui a un renard
        model.arithm(koo, "-", hor, "=", -1).post(); // 12. Les Kools sont fumés dans la maison à côté de celle où se trouve le cheval
        model.arithm(luc, "=", ora).post();   // 13. Le Lucky Strike est fumé avec le jus d'orange
        model.arithm(jap, "=", par).post();   // 14. Le Japonais fume des Parliament
        model.arithm(nor, "=", blu).post();    // 15. Le Norvégien vit à côté de la maison bleue
        
        
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
            System.out.println("Sol "+ model.getSolver().getSolutionCount()+"\n"+model);
	    }
	    
 
        
        // Affichage de l'ensemble des caractéristiques de résolution
      	System.out.println("\n\n*** Bilan ***");        
        model.getSolver().printStatistics();
	}
}

    

