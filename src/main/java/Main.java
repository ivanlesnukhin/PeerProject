import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
            List deadFeatures = new ArrayList();
           int numberOfDeadFeatures = 0;

           for (int i = 1; i <= 1255; i ++){
                IVecInt vecInt = new VecInt(1);
                vecInt.insertFirst(i);
                boolean isSatisfiable = problem.isSatisfiable(vecInt);

                if (!isSatisfiable){
                    numberOfDeadFeatures ++;
                    deadFeatures.add(Integer.toString(i));
                }
            }
           System.out.println("TASK B.2");

           System.out.println("we have: " + numberOfDeadFeatures + " nr of bad values. ");

           System.out.println("these are: " + deadFeatures.toString());

            InputStream file = Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs");
            Scanner scr = null;
            scr = new Scanner(file);
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
            for (int j = 1; j <= 1255; j ++){

                System.out.println(j);
                for (int i = 1; i <= 1255; i ++){
                    if (i != j && !deadFeatures.contains(i)){
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