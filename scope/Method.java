package scope;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * This class represents a method.
 * It extends Scope.
 */
public class Method extends Scope {
	private String methodName;
	private String args;
	public static final String RETURN = "return;";
	private final ArrayList<Variable> arguments = new ArrayList<>();

	/**
	 * This is the constructor of Method.
	 * @param lines lines of method.
	 * @param parent scope parent.
	 */
	public Method(ArrayList<String> lines, Scope parent) throws ScopeException {
		this.lines = lines;
		this.parentScope = parent;
		this.type = METHOD;

		isReturnExists();
		setMethodArgs();
		buildScopeTree();
	}

	/**
	 * Finds variable name in parent scope.
	 * @param name variable name.
	 * @return if found the Variable object of the given variable name, else null.
	 */
	public Variable getVarFromName(String name) throws VariableException {
		if (nameToVar.get(name) == null) {
			Variable parentVar = parentScope.getVarFromName(name);
			if (parentVar != null) {
				Variable newVar = new Variable(parentVar.getType(), parentVar
						.getValue(), parentVar.getName(), parentVar.isConstant());
				addVar(newVar);
				return newVar;
			}
		}
		return  nameToVar.get(name);
	}

	/**
	 * Checks if the method ends with a return command.
	 */
	private void isReturnExists()throws ScopeException {
		String returnString = lines.get(lines.size() - 2);
		if (!returnString.matches(RETURN)) {
			throw new ReturnException();
		}
	}

	/**
	 * Sets the arguments of the method.
	 */
	private void setMethodArgs() {
		String firstLine = lines.get(0);
		Matcher methodMatch = METHOD_PATTERN.matcher(firstLine);
		methodMatch.find();
		methodName = methodMatch.group(1);
		args = methodMatch.group(2);
		lines.remove(0);
	}

	/**
	 * Gets method's name.
	 * @return method's name.
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Gets method's arguments.
	 * @return method's arguments.
	 */
	public String getMethodArgs() {
		return args;
	}

	/**
	 * Adds an argument to arguments ArrayList.
	 * @return true if added successfully.
	 */
	public boolean addArg(Variable argument)throws SameNameException {
		if (arguments.contains(argument)) {
			throw new SameNameException(argument);
		}
		return arguments.add(argument);
	}

	/**
	 * Gets arguments of method.
	 * @return arguments of method.
	 */
	public ArrayList<Variable> getArgs() {
		return arguments;
	}



}

