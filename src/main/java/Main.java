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
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter("src/main/resources/output.txt"));

            boolean unsat = true;
           IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("small.dimacs"));

           System.out.println("TASK B.1");
            outputWriter.write("TASK B.1" + "\n");
           if(problem.isSatisfiable()){
               System.out.println("Satisfiable");
               outputWriter.write("Satisfiable" + "\n");
           }else{
               System.out.println("Not satisfiable");
               outputWriter.write("Not satisfiable" + "\n");
           }

//HERE STARTS TASK B.2 -----------------------------------------------------------------------------------------------------------
            InputStream file = Main.class.getClassLoader().getResourceAsStream("small.dimacs");
            Scanner scan = new Scanner(file);
            List <Integer> numbersinFile = new ArrayList<>();
            HashMap<Integer, String> hmap = new HashMap<Integer, String>();
            Set<Integer> hash_Set  = new HashSet<Integer>();
            List <String> deadFeatureNames = new ArrayList<>();

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
                    deadFeatureNames.add(hmap.get(i));
                }

            }

            System.out.println("TASK B.2");
            outputWriter.write("TASK B.2" + "\n");

            System.out.println("The names of the dead features are: ");
            outputWriter.write("The names of the dead features are: " + "\n");

            for (String s: deadFeatureNames){
                System.out.println(s);
            }

            System.out.println("We have: " + numberOfDeadFeatures + " nr of dead features. ");
            System.out.println("We have: " + numberOfDeadFeatures + " nr of dead features. " + "\n");

            Scanner scr = new Scanner(file);
            int counter = 0;
            while(scr.hasNext() && counter<deadFeatures.size()){
                if(deadFeatures.contains(scr.next())) {
                    System.out.println(scr.next());
                    System.out.println(scr.next() + "\n");
                    counter++;
                }
            }
            



//HERE starts TASK B.3 --------------------------------------------------------------------------------------------------------
            IProblem problemMini = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));
            BufferedWriter implicationWriter = new BufferedWriter(new FileWriter("src/main/resources/implications.txt"));

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
                            implicationWriter.write(i + " is dependant on " + j + "\n");
                        }
                    }
                }
            }

            implicationWriter.close();

            System.out.println("TASK B.3");
            outputWriter.write("TASK B.3" + "\n");


            System.out.println("we have: " + numberOfDependencies + " nr of dependencies. ");
            outputWriter.write("we have: " + numberOfDependencies + " nr of dependencies. " + "\n");

            outputWriter.close();


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