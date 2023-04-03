package oop.ex6.main;

import parsing.MainParser;
import parsing.ParseException;
import scope.Scope;
import scope.ScopeException;
import scope.VariableException;
import parsing.SyntaxParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main class - parses a sjava file and checks if it's valid.
 */
public class Sjavac {
    private static final String VALID = "0";
    private static final String INVALID = "1";
    private static final String EXCEPTION = "2";

    /**
     * Main method of Sjava.
     */
    public static void main(String[] args) {
        try {

            BufferedReader sourceFile = new BufferedReader(new FileReader(args[0]));

            ArrayList<String> cleanSourceCode = SyntaxParser.validateSyntax(sourceFile);

            // create scopes recursively, starting from global scope
            Scope globalScope = new Scope(cleanSourceCode, null);

            // parse global scope (and inner scopes)
            MainParser.mainParse(globalScope);
            System.out.println(VALID); // program ends here if valid

        } catch (IOException e) {
            System.out.println(EXCEPTION);
            System.err.println(e.getMessage());
        } catch (ScopeException | ParseException | VariableException e) {
            System.out.println(INVALID);
            System.err.println(e.getMessage());
        }
    }
}
