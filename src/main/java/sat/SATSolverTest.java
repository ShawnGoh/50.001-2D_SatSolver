package sat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;


public class SATSolverTest {

    public static Object[] readFile(String directory) {

        try {
            FileInputStream fis = new FileInputStream(directory);
            Scanner sc = new Scanner(fis);
            Object[] output = new Object[2];
            Integer noLiterals = 0;
            String line = sc.nextLine();

            //Find p and get number of literals
            while (line.charAt(0) != 'p') {
                line = sc.nextLine();
                if (line.charAt(0) == 'p') {
                    String[] params = line.split(" ");
                    noLiterals = Integer.parseInt(params[2]);}}

            //Initialize a Imlist to store clauses
            ImList<Clause> clauses = new EmptyImList<Clause>();

            //Iterate through lines following problem statement line and adding literal to clause, then clause to the clauses Imlist
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.length() > 0) {
                    String[] split = line.split(" ");
                    Clause clause = new Clause();
                    for (String l : split) {
                        if (l.length() > 0) {
                            if (Integer.parseInt(l) == 0){}
                            else if (l.charAt(0) == '-') {clause = clause.add(NegLiteral.make(l.replace("-", ""))); }
                            else { clause = clause.add(PosLiteral.make(l)); }
                        }
                    }
                    clauses = clauses.add(clause);
                }
            }
            //return as an Object[]  with clauses in 0 position and number of literals in 1 position
            output[0] = clauses;
            output[1] = noLiterals;
            return output;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IOException {
        //Accepts String[] args and the first value args[0] is the directory of the CNF file and is passed to the parser function readFile (directory) .
        String directory = args[0];
        Object[] output = readFile(directory);
        ImList<Clause> clauses = (ImList<Clause>) output[0];
        int noliterals = (int)output[1];

        //A formula is then created from the resulting clause list and is passed into the solve(Formula formula) .
        Formula f = new Formula();
        for(Clause c: clauses){f=f.addClause(c); }

        //Solve + timekeeping portion
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();

        Environment finalresult = SATSolver.solve(f);

        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");

        //If resulting result is null, just print not satisfiable.
        if(finalresult==null){
            System.out.println("Not Satisfiable");}

        //If the resulting environment finalresult(solve result) is not null, an output file is then generated with FileWriter myWriter=new FileWriter("BoolAssignment.txt").
        else{
            System.out.println("Satisfiable, writing to boolean combination to BoolAssignment.txt");
        try {
            FileWriter myWriter = new FileWriter("BoolAssignment.txt");
            //An iteration through all the variables is done by a for loop for(int i = 1; i<noliterals+1 ; i++) where each iteration prints out the boolean value of the literal from the environment file from the solve method
            for(int i = 1; i<noliterals+1 ; i++){
                myWriter.write(String.valueOf(new Variable(i+ ":"+ finalresult.get(new Variable(Integer.toString(i))).toString() +"\n")));
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }}
    }
}