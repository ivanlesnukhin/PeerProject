import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.*;
import org.sat4j.tools.ModelIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.sat4j.reader.Reader;

public class Main {
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
}

