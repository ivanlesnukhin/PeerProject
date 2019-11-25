import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.InstanceReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import org.sat4j.reader.Reader;

public class Main {
    public static void main(String[] args) {

        ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        PrintWriter out = new PrintWriter(System.out,true);


        // filename is given on the command line
        try {
            boolean unsat = true;
           IProblem problem = reader.parseInstance(Main.class.getClassLoader().getResourceAsStream("ecos_x86.dimacs"));

           if(problem.isSatisfiable()){
               System.out.println("Satisfiable");
           }else{
               System.out.println("Not satisfiable");
           }
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

