package parsing;

import scope.Method;
import scope.Scope;
import scope.ConditionsException;
import scope.ScopeException;
import scope.MethodException;
import scope.Variable;
import scope.AssignException;
import scope.VariableException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The main parser that parse the file line by line.
 */

public class MainParser {
    public static final String BOOLEANS = "boolean|double|int";
    public static final String VAR_NAME = " ?(?!" + BOOLEANS + "|char|String|void|final|if|while)" +
            "[a-zA-Z]+\\w*|_\\w+";

    public static final Pattern VAR_ASSIGN =
            Pattern.compile("(" + VAR_NAME + ")\\s*=\\s*([^;,]+)\\s*(;|,)?");
    public static final Pattern CONDITION_PATTERN = Pattern.compile("(if|while)\\s*\\((.+)\\)\\s*\\{?");
    public static final Pattern VAR_NAME_PATTERN = Pattern.compile(VAR_NAME);
    public static final Pattern METHOD_CALL_PATTERN =
            Pattern.compile("([a-zA-Z]+\\w*)\\s*\\((.*)\\)\\s*;");
    public static final Pattern VAR_DECL_PATTERN =
            Pattern.compile("(final\\s)?\\s*(" + BOOLEANS + "|char" + "|String)\\s+([^;]+);?");

    public static final String CONDITION_DEL = "(\\|\\|)|(&&)";
    public static final String ARGUMENT_DEL = "\\s*,\\s*";
    public static final String VALID_LINES = "\\s*return\\s*;|void\\s+([a-zA-Z]+\\w*)\\s*\\((.*)\\)\\s*|}";
    public static final String RETURN = "\\s*return\\s*;";

    private static final String BOOLEAN_TYPE = "boolean";
    private static final String INT_TYPE = "int";
    private static final String DOUBLE_TYPE = "double";
    private static final String DEFAULT_BOOLEAN = "1";
    private static final String STRING_TYPE = "String";
    private static final String DEFAULT_STRING = "\"\"";
    private static final String CHAR_TYPE = "char";
    private static final String DEFAULT_CHAR = "\' \'";

    /**
     * Parses the file.
     *
     * @param scope current scope.
     */
    public static void mainParse(Scope scope) throws VariableException, ScopeException, ParseException {
        parseLines(scope);
        parseMethod(scope);
    }

    /**
     * Get the default value for each type
     *
     * @param type
     * @return
     */
    private static String getDefaultValueByType(String type) {
        if (type.equals(BOOLEAN_TYPE) || type.equals(INT_TYPE) || type.equals(DOUBLE_TYPE)) {
            return DEFAULT_BOOLEAN;
        } else if (type.equals(STRING_TYPE)) {
            return DEFAULT_STRING;
        } else if (type.equals(CHAR_TYPE)) {
            return DEFAULT_CHAR;
        }
        return null;
    }

    /**
     * Parses all lines of the file.
     *
     * @param scope current scope.
     */
    private static void parseLines(Scope scope) throws VariableException, ScopeException, ParseException {
        for (String line : scope.getLines()) {
            Matcher varMatcher = VAR_ASSIGN.matcher(line);
            Matcher methodMatcher = METHOD_CALL_PATTERN.matcher(line);
            Matcher condMatcher = CONDITION_PATTERN.matcher(line);
            if (VAR_DECL_PATTERN.matcher(line).matches()) {
                parseVarDec(scope, line);
            } else if (varMatcher.matches()) {
                parseVarAssign(scope, true, varMatcher);
            } else if (condMatcher.matches()) {
                if (!isCondLegal(scope, condMatcher.group(2).split(CONDITION_DEL))) {
                    throw new ConditionsException();
                }
                if (scope.getParentScope() == null) { // condition may only appear inside a method
                    throw new ConditionsException();
                }
                mainParse(Objects.requireNonNull(scope.getConditionScopes().pollFirst()));
            } else if (methodMatcher.find()) {
                if (scope.getParentScope() == null) {
                    throw new SyntaxException(line);
                }
                parseMethod(scope, methodMatcher);
            } else if (!line.matches(VALID_LINES)) {
                throw new SyntaxException(line);
            } else if (line.matches(RETURN) && scope.getParentScope() == null) {
                throw new SyntaxException(line);
            }
        }
    }


    /**
     * Parses method.
     *
     * @param scope current scope.
     */
    private static void parseMethod(Scope scope) throws VariableException, ScopeException, ParseException {
        for (Method temp : scope.getMethodScopes()) {
            setArgs(temp);
            mainParse(temp);
        }
    }

    /**
     * sets arguments of method.
     *
     * @param method current method.
     */
    private static void setArgs(Method method) throws VariableException, SyntaxException {
        if (method.getArgs().isEmpty()) {
            for (String argument : method.getMethodArgs().split(ARGUMENT_DEL)) {
                if (argument.length() != 0) {
                    Matcher varDecMatch = VAR_DECL_PATTERN.matcher(argument.trim());
                    if (!varDecMatch.matches()) {
                        throw new AssignException(argument);
                    } else {
                        String varType = varDecMatch.group(2).trim();
                        if (method.addArg(new Variable(varType, getDefaultValueByType(varType),
                                varDecMatch.group(3).trim(), false))) {
                            parseVarDec(method, argument + "=" + getDefaultValueByType(varType));
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if condition is legal.
     *
     * @param scope current scope.
     * @param cond  current condition.
     * @return true if condition id legal.
     */
    private static boolean isCondLegal(Scope scope, String[] cond) throws VariableException {
        for (String arg : cond) {
            Variable var = scope.getVarFromName(arg.trim());
            if (var != null && var.getValue() != null && scope.getVarFromName(arg.trim()).getType()
                    .matches(BOOLEANS)) {
                continue;
            }
            if (var != null && var.getValue() != null && !scope.getVarFromName(arg.trim()).getType()
                    .matches(BOOLEANS)) {
                return false;
            }
            if (var != null && var.getValue() == null) {
                return false;
            }

            boolean isABooleanArgument = arg.trim().matches(Variable.BOOLEAN_PATTERN);
            boolean isAValidVariable = arg.trim().matches(VAR_NAME);
            if (!isABooleanArgument && !isAValidVariable) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses variable declaration.
     *
     * @param scope current scope.
     * @param line  current line.
     */
    private static void parseVarDec(Scope scope, String line) throws VariableException, SyntaxException {
        Matcher varDeclMatcher = VAR_DECL_PATTERN.matcher(line);
        varDeclMatcher.find();
        for (String var : varDeclMatcher.group(3).split(",")) {
            Matcher varAssignMatcher = VAR_ASSIGN.matcher(var.trim());
            Matcher varWithoutAssignment = VAR_NAME_PATTERN.matcher(var.trim());
            if (varWithoutAssignment.matches()) {
                if (varDeclMatcher.group(1) != null) {
                    throw new AssignException(var);
                }
                scope.addVar(new Variable(varDeclMatcher.group(2).trim(), null,
                        varWithoutAssignment.group(0).trim(), false));
            } else if (varAssignMatcher.matches()) {
                scope.addVar(new Variable(varDeclMatcher.group(2).trim(), null,
                        varAssignMatcher.group(1).trim(), false));
                parseVarAssign(scope, varDeclMatcher.group(1) != null, varAssignMatcher);
            } else {
                throw new SyntaxException(line);
            }
        }
    }

    /**
     * Parses variable assignment.
     *
     * @param scope      current scope.
     * @param isConstant current line.
     * @param matcher    variable assignment matcher.
     */
    private static void parseVarAssign(Scope scope, boolean isConstant, Matcher matcher)
            throws VariableException {

        String varName = matcher.group(1);
        Variable curVar = scope.getVarFromName(varName);

        if (curVar == null || curVar.isConstant()) {
            throw new AssignException(varName);
        }

        if (parseRef(scope, matcher, varName, curVar)) {
            return;
        } else {
            curVar.setValue(matcher.group(2));
        }
        if (isConstant) {
            curVar.setConstant(true);
        }
    }

    /**
     * Checks if an assigned value is another variable.
     *
     * @param scope   current scope.
     * @param matcher variable assignment matcher.
     * @param curVar  current variable.
     */
    private static boolean parseRef(Scope scope, Matcher matcher, String varName, Variable curVar) throws
            VariableException {
        String newVarName = matcher.group(2);
        Variable refVar = scope.getVarFromName(newVarName);
        if (refVar != null) {
            String refType = refVar.getType();
            String curType = curVar.getType();
            boolean isValidBoolean =
                    (curType.equals(BOOLEAN_TYPE) && (refType.equals(BOOLEAN_TYPE) ||
                            refType.equals(INT_TYPE) || refType.equals(DOUBLE_TYPE)));
            boolean isValidDouble =
                    (curType.equals(DOUBLE_TYPE) && (refType.equals(INT_TYPE) ||
                            refType.equals(DOUBLE_TYPE)));
            if (!refVar.getType().equals(curType) && !isValidBoolean && !isValidDouble) {
                throw new AssignException(varName);
            } else {
                if (refVar.getValue() == null) {
                    throw new AssignException(varName);
                } else {
                    curVar.setValue(refVar.getValue());
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Parses current method.
     *
     * @param scope         current scope.
     * @param methodMatcher matcher for method.
     */
    private static void parseMethod(Scope scope, Matcher methodMatcher) throws ScopeException,
            VariableException, ParseException {
        String[] methodArgs = new String[0];
        if (!methodMatcher.group(2).isEmpty()) {
            methodArgs = methodMatcher.group(2).trim().split(ARGUMENT_DEL);
        }
        findMethod(scope, methodMatcher, methodArgs);
    }


    /**
     * Finds the method we currently call.
     *
     * @param scope         current scope.
     * @param methodMatcher matcher for method.
     * @param args          arguments of current method.
     */
    private static void findMethod(Scope scope, Matcher methodMatcher, String[] args)
            throws MethodException, VariableException, ParseException {
        Method foundedMethod = null;
        Scope scopeCopy = scope;
        while (scopeCopy != null) {
            for (Method temp : scopeCopy.getMethodScopes()) {
                if (!temp.getMethodName().equals(methodMatcher.group(1))) {
                    continue;
                }
                foundedMethod = temp;
            }
            scopeCopy = scopeCopy.getParentScope();
        }
        if (foundedMethod == null) {
            throw new MethodException(methodMatcher.group(1));
        }
        setArgs(foundedMethod);
        scope.getMethodScopes().remove(foundedMethod);
        ArrayList<Variable> signatureArgs = foundedMethod.getArgs();
        parseArgs(signatureArgs, args, scope);
    }

    /**
     * Parses current method.
     *
     * @param signatureArgs arguments in method signature.
     * @param callArgs      arguments of the method we currently try to call.
     * @param scope         current scope.
     */
    private static void parseArgs(ArrayList<Variable> signatureArgs, String[] callArgs, Scope scope)
            throws ParseException, VariableException {

        if (signatureArgs.size() != callArgs.length) {
            throw new NumArgsException();
        }
        for (int i = 0; i < callArgs.length; i++) {
            Variable checkedVarName = scope.getVarFromName(callArgs[i]);
            if (checkedVarName != null) {
                signatureArgs.get(i).setValue(checkedVarName.getValue());
            } else {
                signatureArgs.get(i).setValue(callArgs[i]);
            }
        }
    }

}
