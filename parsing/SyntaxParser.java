package parsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class is responsible to check if the syntax of the code is valid.
 */
public class SyntaxParser {
    private static final char SEMICOLON = ';';
    private static final String IO_ERROR_CODE = "2";
    private static final String ONLY_SPACE_LINES = "^(\\s)+$";
    private static final String LEGAL_COMMENT = "//.*";
    private static final String EMPTY_STRING = "";
    private static final char ROUND_BRACKET_START = '(';
    private static final char ROUND_BRACKET_END = ')';
    private static final char CURLY_BRACKET_START = '{';
    private static final char CURLY_BRACKET_END = '}';


    /**
     * Main method that checks if the syntax is valid.
     */
    public static ArrayList<String> validateSyntax(BufferedReader bufferReader) throws FileNotFoundException,
            SyntaxException {
        ArrayList<String> sourceCodeLines = bufferToArrayList(bufferReader);
        sourceCodeLines = clean(sourceCodeLines);
        checkBracketsValidity(sourceCodeLines);
        return sourceCodeLines;
    }

    /**
     * Converts the bufferReader to an ArrayList.
     * @param bufferReader file represented as a BufferedReader.
     * @return file represented as an ArrayList.
     */
    private static ArrayList<String> bufferToArrayList (BufferedReader bufferReader) {
        try {
            String line;
            ArrayList<String> returnedList = new ArrayList<>();
            while ((line = bufferReader.readLine()) != null) {
                returnedList.add(line);
            }
            return returnedList;

        } catch (IOException e) {
            System.out.println(IO_ERROR_CODE);
            return null;
        }
    }

    /**
     * cleans file from comments, whitespaces and empty lines.
     * @param sourceCodeLines file represented as an ArrayList.
     * @return cleaned file.
     */
    private static ArrayList<String> clean(ArrayList<String> sourceCodeLines) {
        ArrayList<String> cleanedLines = new ArrayList<>();

        for (String line : sourceCodeLines) {
            line = line.replaceAll(ONLY_SPACE_LINES, EMPTY_STRING);
            // remove all comments from the file
            line = line.replaceAll(LEGAL_COMMENT, EMPTY_STRING);
            line = line.trim(); // remove all leading and trailing spaces
            // if not empty line
            if (!line.equals(EMPTY_STRING)) {
                cleanedLines.add(line);
            }

        }
        return cleanedLines;
    }

    /**
     * checks if brackets in the code are valid.
     * @param sourceCodeLines file represented as an ArrayList.
     */
    private static void checkBracketsValidity(Iterable<String> sourceCodeLines) throws SyntaxException {
        int curlyBracketCounter = 0;
        Iterator<String> sourceIter = sourceCodeLines.iterator(); // iterate over iterable source file code
        String currentLine = EMPTY_STRING;
        while (sourceIter.hasNext()) { // run over whole file, line by line
            currentLine = sourceIter.next();
            char[] lineAsCharArray = currentLine.toCharArray(); // look at line as char array

            int roundBracketCounter = 0;
            for (int i = 0; i < lineAsCharArray.length; i++) { // check if char is a bracket
                switch (lineAsCharArray[i]) {
                    case (ROUND_BRACKET_START):
                        roundBracketCounter++;
                        break;
                    case (ROUND_BRACKET_END):
                        roundBracketCounter--;
                        break;
                    case (CURLY_BRACKET_START): // a curly bracket opening can only be the last char
                        if (i != lineAsCharArray.length - 1) {
                            throw new SyntaxException(currentLine);
                        } else
                            curlyBracketCounter++;
                        break;
                    case (CURLY_BRACKET_END): // a curly bracket must be in a separate line
                        if (lineAsCharArray.length != 1)
                            throw new SyntaxException(currentLine);
                        else
                            curlyBracketCounter--;
                        break;
                    case (SEMICOLON):
                        if (i != lineAsCharArray.length - 1) // semicolon must be the last char
                            throw new SyntaxException(currentLine);
                        break;
                }
                if (roundBracketCounter < 0) {
                    throw new SyntaxException(currentLine);
                }
            } // check last char validity
            char lastChar = lineAsCharArray[lineAsCharArray.length - 1];
            if (roundBracketCounter != 0 || lastChar != CURLY_BRACKET_START && lastChar != SEMICOLON
                    && lastChar != CURLY_BRACKET_END) {
                throw new SyntaxException(currentLine);
            }
        }
        if (curlyBracketCounter != 0)
            throw new SyntaxException(currentLine);
    }
}
