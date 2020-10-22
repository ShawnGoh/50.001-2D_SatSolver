package sat;

import immutable.EmptyImList;
import immutable.ImList;
import sat.env.Bool;
import sat.env.Environment;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.Literal;
import sat.formula.NegLiteral;
import sat.formula.PosLiteral;

public class SATSolver {

    //calls the method formula.getClauses() to retrieve the clauses which are passed as a parameter with a new Environment() in the below solve method. solve(formula.getClauses(), new Environment())
    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(), new Environment());
    }

    private static Environment solve(ImList<Clause> clauses, Environment env) {
        //Step 1 If there are no clauses in clauses (by checking if clauses.isEmpty()), return the environment as-is.
        if (clauses.isEmpty())
        {return env;}

        Clause smallestClause= clauses.first();
        for (Clause c : clauses) {
            //Step 2 If there are any empty clauses, it signifies a FALSE boolean value. This would return null .
            if (c.isEmpty()) {
                return null;
            }
            //Step 3 If there are no empty clauses, proceed to find the clause with the smallest clause size, saving it as smallestClause .
            else if (c.size() == 1) {
                smallestClause = c;
                break;
            } else if (c.size() < smallestClause.size()) {
                smallestClause = c;
            }
        }
        //Step 4 The first literal in smallestClause will be saved as Literal l = smallestClause.chooseLiteral()
        Literal l = smallestClause.chooseLiteral();

        //Step 5 Depending on the size of the min_clause, we will decide on what to do:
        //Step 5a. If smallestClause is a unit clause (size of 1), the method will assign it a corresponding boolean value, adding it to the current
        //environment(env=env.put(l.getVariable(), isNeg ? Bool.FALSE:Bool.TRUE)) such that the clause is eliminated from the
        //boolean statement(clauseList=substitute(clauses , l). Thereafter, return the solve method solve(clauseList , env).
        if (smallestClause.size() == 1) {
            boolean isNeg = l instanceof NegLiteral;
            env = env.put(l.getVariable(), isNeg ? Bool.FALSE : Bool.TRUE);
            ImList clauseList = substitute(clauses, l);
            return solve(clauseList,env);
        }

        //Step 5b. If smallestClause is not a unit clause, l will first be assigned as Bool.TRUE in a new environment ( positiveEnv
        //=env.putTrue(l.getVariable())). The current clause list (clauses) and l with a TRUE value is passed through the substitute
        //method and assigned as a new variable( bePositive=substitute(clauses, positiveL) ). They are then recursively fed into the
        //solve method(solve(bePositive, positiveEnv)). Return the resulting environment if it is not null.
        else{
            Environment positiveEnv = env.putTrue(l.getVariable());
            Literal positiveL = PosLiteral.make(l.getVariable());
            ImList bePositive = substitute(clauses, positiveL);
            Environment testEnv1 = solve(bePositive, positiveEnv);
            if (testEnv1 != null) {
                return testEnv1;
            }

            //If the resulting environment has a null value, we will backtrack and l will be assigned as Bool.FALSE in a new
            //environment(negativeEnv = env.putFalse(l.getVariable()) ). The current clause list (clauses) and l with a FALSE value is
            //passed through the substitute method and assigned as a new variable( beNegative = substitute(clauses ,
            //negativeL) Regardless of the value, return the resulting environment of the solve method solve(beNegative , negativeEnv))
            else {
                Environment negativeEnv = env.putFalse(l.getVariable());
                Literal negativeL = NegLiteral.make(l.getVariable());
                ImList beNegative = substitute(clauses, negativeL);
                Environment testEnv2 = solve(beNegative, negativeEnv);
                return testEnv2;
            }
        }}

    //initialises an empty ImList (newClauses) to store the new clauses in, then
    //iterating through the clauses to call reduce(l) to set the literal to true or false in the clause list. The new clause is added to the
    //empty ImList in each loop, returning newClauses after iteration has concluded.
    private static ImList<Clause> substitute(ImList<Clause> clauses,
                                             Literal l) {
        ImList<Clause> newClauses = new EmptyImList<Clause>();
        for (Clause c : clauses) {
            Clause newClause = c.reduce(l);
            if (newClause != null) {
                newClauses = newClauses.add(newClause);
            }
        }
        return newClauses;

    }

}
