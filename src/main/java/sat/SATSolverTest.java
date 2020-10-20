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

//    Literal a = PosLiteral.make("a");
//    Literal b = PosLiteral.make("b");
//    Literal c = PosLiteral.make("c");
//    Literal na = a.getNegation();
//    Literal nb = b.getNegation();
//    Literal nc = c.getNegation();

//    public void testSATSolver1(){
//    	// (a v b)
//    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
///*
//    	assertTrue( "one of the literals should be set to true",
//    			Bool.TRUE == e.get(a.getVariable())
//    			|| Bool.TRUE == e.get(b.getVariable())	);
//
//*/
//    }
//
//
//    public void testSATSolver2(){
//    	// (~a)
//    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
///*
//    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
//*/
//    }
//
//    private static Formula makeFm(Clause... e) {
//        Formula f = new Formula();
//        for (Clause c : e) {
//            f = f.addClause(c);
//        }
//        return f;
//    }
//
//    private static Clause makeCl(Literal... e) {
//        Clause c = new Clause();
//        for (Literal l : e) {
//            c = c.add(l);
//        }
//        return c;
//    }
//


	// TODO: add the main method that reads the .cnf file and calls SATSolver.solve to determine the satisfiability

    public static Object[] readFile(String directory) {

        try {
            FileInputStream fis = new FileInputStream(directory);
            Scanner sc = new Scanner(fis);
            Object[] output = new Object[2];
            String line = sc.nextLine();
            while (line.charAt(0) != 'p') {
                line = sc.nextLine();
            }
//            To parse the clauses

            ImList<Clause> clauses = new EmptyImList<Clause>();
            ImList<Variable> variables = new EmptyImList<Variable>();

            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.length() > 0) {
                    String[] split = line.split(" ");
                    Clause clause = new Clause();
                    for (String l : split) {
                        if (l.length() > 0) {
                            if (Integer.parseInt(l) == 0){}
                            else if (l.charAt(0) == '-') {
                                clause = clause.add(NegLiteral.make(l.replace("-", "")));
                                Variable variable = new Variable(l.replace("-", ""));
                                if(!variables.contains(variable)){variables = variables.add(variable);}
                            } else {
                                clause = clause.add(PosLiteral.make(l));
                                Variable variable = new Variable(l);
                                if(!variables.contains(variable)){variables = variables.add(variable);}
                            }
                        }
                    }
                    clauses = clauses.add(clause);
                }
            }

            output[0] = clauses;
            output[1] = variables;
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
        ImList<Variable> variables = (ImList<Variable>)output[1];

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
            for(Variable v : variables){
                myWriter.write(v.toString()+ ":"+ finalresult.get(v).toString() +"\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }}


    }


    
}