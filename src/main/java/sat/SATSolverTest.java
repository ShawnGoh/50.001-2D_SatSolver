package sat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
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

    public static ImList<Clause> readFile(String directory) {
//        Object[] retArray = new Object[3];
//        To find the parameters:
        try {
            FileInputStream fis = new FileInputStream(directory);
            Scanner sc = new Scanner(fis);
//            Integer noLiterals = 0;
//            Integer noClauses = 0;
            String line = "null";
            while (line.charAt(0) != 'p') {
                line = sc.nextLine();
//                try {
//                    if (line.charAt(0) == 'p') {
//                        String[] params = line.split(" ");
//                        noLiterals = Integer.parseInt(params[2]);
//                        if (params[3].length()>0) {
//                            noClauses = Integer.parseInt(params[3]);
//                        }
//                        else{
//                            noClauses = Integer.parseInt(params[4]);
//                        }
//                    }
//                } catch (Exception e) {
//                    System.out.println("out of index");
//                }
            }

//            To parse the clauses

            ImList<Clause> clauses = new EmptyImList<Clause>();

            while (sc.hasNextLine()) {
                line = sc.nextLine();
                if (line.length() > 0) {
                    String[] split = line.split(" ");
                    Clause clause = new Clause();
                    for (String l : split) {
                        if (l.length() > 0) {
                            if (Integer.parseInt(l) == 0){
                                continue;
                            }
                            else if (l.charAt(0) == '-') {
                                clause = clause.add(NegLiteral.make(l.replace("-", "")));
                            } else {
                                clause = clause.add(PosLiteral.make(l));
                            }

                        }

                    }
                    clauses = clauses.add(clause);
                }
            }
//            retArray[0] = clauses;
//            retArray[1] = noLiterals;
//            retArray[2] = noClauses;
            return clauses;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    } //TODO clean up readfile and try to optimize

    public static void main(String[] args) throws IOException {
//        String directory = "C:\\Users\\Admin\\Documents\\GitHub\\50.001-2D_SatSolver\\src\\main\\test cases\\test_2020.cnf";
//        String directory = "C:\\Users\\Shawn\\AndroidStudioProjects\\JavaPracticeSchool\\code2d\\src\\main\\test cases\\sat2.cnf"; //TODO change to accept string[0] as directory link
        String directory = args[0];
        ImList<Clause> clauses = readFile(directory);
//        int noliterals = (int)output[1];
//        int noclauses = (int)output[2];

        Formula f = new Formula();
        for(Clause c: clauses){
            f=f.addClause(c);
        }

        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();

        Environment finalresult = SATSolver.solve(f);

        long time = System.nanoTime();
        long timeTaken= time - started;
        System.out.println("Time:" + timeTaken/1000000.0 + "ms");

        if(finalresult!=null){
        try {
            FileWriter myWriter = new FileWriter("sat3env.txt"); //TODO change to dynamic implementation of file name
            myWriter.write(finalresult.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }}


    }


    
}