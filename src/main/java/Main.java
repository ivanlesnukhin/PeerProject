import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.sat4j.reader.Reader;

public class Main {

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
    public static void main(String[] args) {

        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out,true);


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

            InputStream file = Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs");
            Scanner scan = new Scanner(file);
            List <Integer> numbersinFile = new ArrayList<>();
            HashMap<Integer, String> hmap = new HashMap<Integer, String>();
            Set<Integer> hash_Set  = new HashSet<Integer>();

            while(scan.hasNextLine()){
                String str = scan.nextLine();
                if (str.startsWith("c")){
                    String[] strArr = str.split(" ");
                    hmap.put(Integer.parseInt(strArr[1]), strArr[2]);
                    //System.out.println(hmap.get(Integer.parseInt(strArr[1])));
                }
            }

            hash_Set = hmap.keySet();

            List deadFeatures = new ArrayList();
           int numberOfDeadFeatures = 0;

           for (Integer i : hash_Set){
                IVecInt vecInt = new VecInt(1);
                vecInt.insertFirst(i);
                boolean isSatisfiable = problem.isSatisfiable(vecInt);

                if (!isSatisfiable){
                    numberOfDeadFeatures ++;
                    deadFeatures.add(Integer.toString(i));
                }
            }
           System.out.println("TASK B.2");

           System.out.println("We have: " + numberOfDeadFeatures + " nr of dead features. ");

           System.out.println("These are: " + deadFeatures.toString());

            Scanner scr = new Scanner(file);
            int counter = 0;
            while(scr.hasNext() && counter<deadFeatures.size()){
                if(deadFeatures.contains(scr.next())) {
                    System.out.println(scr.next());
                    counter++;
                }
            }


//HERE BEGINS TASK B.3 --------------------------------------------------------------------------------------------------------
            IProblem problemMini = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/outPrint.txt"));

            int numberOfDependencies = 0;
            for (Integer j : hash_Set){
                for (Integer i : hash_Set){
                    if (i != j && !deadFeatures.contains(String.valueOf(i)) && !deadFeatures.contains(String.valueOf(j))){
                        IVecInt vecIntB = new VecInt(2);
                        vecIntB.insertFirst(-j);
                        vecIntB.insertFirst(i);

                        boolean isSatisfiable = problemMini.isSatisfiable(vecIntB);

                        if (!isSatisfiable){
                            numberOfDependencies ++;
                            writer.write(i + " is dependant on " + j + "\n");
                        }
                    }
                }
            }


            writer.close();

            System.out.println("TASK B.3");

            System.out.println("we have: " + numberOfDependencies + " nr of dependencies. ");



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
}