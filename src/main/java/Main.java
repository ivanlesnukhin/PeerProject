import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;

<<<<<<< Updated upstream
import java.io.FileNotFoundException;
import java.io.IOException;
=======
import java.io.*;
>>>>>>> Stashed changes
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.sat4j.reader.Reader;

public class Main {
<<<<<<< Updated upstream
=======
    String filePath = "src/main/resources/ecos_x86.dimacs";
    /*
    public void printName(int num) {
        File file = new File("/src/main/java/Main/ecos_x86.dimacs");
        Scanner scr = null;
        try {
            scr = new Scanner(file);
            while(scr.hasNext()){
                System.out.println("line : "+scr.next());
            }
        } catch (FileNotFoundException ex) {
            System.out.println("F");
        }
    }
*/
>>>>>>> Stashed changes
    public static void main(String[] args) {

        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out,true);
        String filePath = "src/main/resources/ecos_x86.dimacs";


// HERE STARTS TASK B.1 -----------------------------------------------------------------------------------------------------
        try {
            boolean unsat = true;
           IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));

           System.out.println("TASK B.1");
           if(problem.isSatisfiable()){
               System.out.println("Satisfiable");
           }else{
               System.out.println("Not satisfiable");
           }

//HERE STARTS TASK B.2 -----------------------------------------------------------------------------------------------------------
            List badValues = new ArrayList();
            int numberOfBadValues = 0;

            for (int i = 1; i <= 1255; i ++){
                IProblem problemI = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));
                IVecInt vecInt = new VecInt(1);
                vecInt.insertFirst(i);
                boolean isSatisfiable = problemI.isSatisfiable(vecInt);
                if (!isSatisfiable){
                    numberOfBadValues ++;
                    badValues.add(i);
                }
            }
            System.out.println("TASK B.2");

            System.out.println("we have: " + numberOfBadValues + " nr of bad values. ");

            System.out.println("these are: " + badValues.toString());

<<<<<<< Updated upstream
=======
            InputStream file = Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs");
            Scanner scr = new Scanner(file);

            for (int i = 1; i < 1255; i++) {
                IProblem problemI = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));
                IVecInt vecInt = new VecInt(1);
                vecInt.insertFirst(i);

            }

            int counter = 0;
            while(scr.hasNext() && counter<badValues.size()){
                if(badValues.contains(scr.next())) {
                    System.out.println(scr.next());
                    counter++;
                }
            }

            List<String> implications = new ArrayList<>();
            ISolver solver1 = SolverFactory.newDefault();
            solver.setTimeout(3600); // 1 hour timeout
            Reader reader1 = new DimacsReader(solver1);
            IProblem problem1 = reader1.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));

            Map< Integer, String > map = getAllFeatures();
            System.out.println("MAPS:     " + map.toString());

            filePath = "src/main/resources/ecos_x86.dimacs";
            Map< Integer, String > impMap = new HashMap<>();
            FileReader freader = new FileReader(filePath);
            BufferedReader breader = new BufferedReader(freader);

>>>>>>> Stashed changes
//HERE ENDS TASK B.2 --------------------------------------------------------------------------------------------------------

           /**
           while (problem.isSatisfiable()) {
                unsat = false;
                int [] model = problem.model();
                System.out.println("Satisfiable");
            }
            */
            //if (unsat)
                //System.out.println("Non satisfiable");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");
        }
    }

    static Map< Integer, String > getAllFeatures() throws IOException{
        String filePath = "src/main/resources/ecos_x86.dimacs";
        Map< Integer, String > map = new HashMap<>();
        FileReader freader = new FileReader(filePath);
        BufferedReader breader = new BufferedReader(freader);

        while(true){
            String row = breader.readLine();
            if (row.charAt(0) != 'c')
            {
                break;
            }
            row = row.substring(2);
            int ws = row.indexOf(" ");
            String name = row.substring(ws+1);
            int nbr = Integer.valueOf(row.substring(0,ws));
            map.put(nbr,name);

        }
        return map;

    }

}

