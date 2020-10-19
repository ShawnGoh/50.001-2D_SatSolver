package sat;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import immutable.EmptyImList;
import immutable.ImList;
import sat.formula.Clause;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class Parser {
    public static void main(String args[]) {
//        String directory = "C:\\Users\\Admin\\Documents\\GitHub\\50.001-2D_SatSolver\\src\\main\\test cases\\sat1.cnf";
        String directory = "C:\\Users\\Admin\\Documents\\GitHub\\50.001-2D_SatSolver\\src\\main\\test cases\\test_2020.cnf";
        Object[] output = new Object[3];
        output = readFile(directory);
        System.out.print("Clauses: ");
        System.out.println(output[0].toString());
        System.out.print("Number of literals: ");
        System.out.println(output[1]);
        System.out.print("Number of clauses: ");
        System.out.println(output[2]);
    }


    /**
     * Takes a .cnf file and parses it.
     *
     * @param directory
     *            The absolute directory of the file to be found

     * @return an object array, where Object[0] is an ImList containing all clauses
     * in clause objects, Object[1] is the number of literals, and Object[2] is the
     * number of clauses.
     */

    public static Object[] readFile(String directory) {
        Object[] retArray = new Object[3];
//        To find the parameters:
        try {
            FileInputStream fis = new FileInputStream(directory);
            Scanner sc = new Scanner(fis);
            Integer noLiterals = 0;
            Integer noClauses = 0;
            String line = "null";
            while (line.charAt(0) != 'p') {
                line = sc.nextLine();
                try {
                    if (line.charAt(0) == 'p') {
                        String[] params = line.split(" ");
                        noLiterals = Integer.parseInt(params[2]);
                        if (params[3].length()>0) {
                            noClauses = Integer.parseInt(params[3]);
                        }
                        else{
                            noClauses = Integer.parseInt(params[4]);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("out of index");
                }
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
                                clause = clause.add(NegLiteral.make(l.substring(1)));
                            } else {
                                clause = clause.add(PosLiteral.make(l));
                            }
                            clauses = clauses.add(clause);
                        }
                    }
                }
            }
            retArray[0] = clauses;
            retArray[1] = noLiterals;
            retArray[2] = noClauses;
            return retArray;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    return null;
    }
}
