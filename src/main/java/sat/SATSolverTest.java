package sat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
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
            while (line.charAt(0) != 'p') {
                line = sc.nextLine();
                if (line.charAt(0) == 'p') {
                    String[] params = line.split(" ");
                    noLiterals = Integer.parseInt(params[2]);}}

            ImList<Clause> clauses = new EmptyImList<Clause>();

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

        String directory = args[0];
        Object[] output = readFile(directory);
        ImList<Clause> clauses = (ImList<Clause>) output[0];
        int noliterals = (int)output[1];

        Formula f = new Formula();
        for(Clause c: clauses){f=f.addClause(c); }

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();

        Environment finalresult = SATSolver.solve(f);

        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");

        if(finalresult==null){
            System.out.println("Not Satisfiable");}

        else{
            System.out.println("Satisfiable, writing to boolean combination to BoolAssignment.txt");
        try {
            FileWriter myWriter = new FileWriter("BoolAssignment.txt");
            myWriter.write("Boolean Assignment for Satisfied Equation\n");
            int undefined = 0;
            int truecount = 0;
            int falsecount = 0;

            for(int i = 1; i<noliterals+1 ; i++){
                if(finalresult.get(new Variable(Integer.toString(i)))== Bool.TRUE){
                    truecount++; }
                else if(finalresult.get(new Variable(Integer.toString(i)))==Bool.FALSE){
                    falsecount++; }
                else{undefined++;}

                myWriter.write(String.valueOf(new Variable(i+ ":"+ finalresult.get(new Variable(Integer.toString(i))).toString() +"\n")));
            }

            System.out.println("True: " + truecount + " False: "+ falsecount + " Undefined: "+undefined);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }}


    }


    
}