package scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents a scope.
 */
public class Scope {

	private static final String MAIN = "Main";
	protected static final String METHOD = "Method";
	public static final Pattern METHOD_PATTERN =
			Pattern.compile("void (?!int|double|String|boolean|char"+
					"|void|final|if|while"
					+")([a-zA-Z]+\\w*) ?\\((.*)\\) ?\\{");
	public static final Pattern CONDITION_PATTERN =
			Pattern.compile("(if|while)\\s*\\((.+)\\)\\s*\\{?");

	protected ArrayList<String> lines;
	protected Scope parentScope;
	protected String type;
	private final LinkedList<Scope> conditionScopes = new LinkedList<>();
	private final LinkedList<Method> methodScopes = new LinkedList<>();
	protected final HashMap<String, Variable> nameToVar = new HashMap<>();


	/**
	 * This is an empty constructor (so we don't need to call super() in Method class that extends Scope).
	 */
	public Scope() {}

	/**
	 * This is the constructor of Scope.
	 * @param lines lines of scope.
	 * @param parent scope parent.
	 */
	public Scope(ArrayList<String> lines, Scope parent) throws ScopeException {
		this.lines = lines;
		this.parentScope = parent;
		if (parent == null) {
			lines.add(0, " ");
			lines.add(lines.size(), " ");
			type = MAIN;
			buildScopeTree();
			lines.remove(0);
			lines.remove(lines.size()-1);
		} else {
			setScope();
			buildScopeTree();
		}
	}

	/**
	 * Sets scope's parameters before building the scope tree.
	 */
	private void setScope() {
		Matcher conditionMatch = CONDITION_PATTERN.matcher(lines.get(0));
		conditionMatch.find();
		type = conditionMatch.group(1);
		lines.remove(0);
	}

	/**
	 * Builds the scope tree.
	 */
	protected void buildScopeTree() throws ScopeException {
		Pattern startBrackPattern = Pattern.compile("\\{");
		Pattern endBrackPattern = Pattern.compile("\\}");
		ListIterator<String> linesIterator = lines.listIterator();

		// go through current scope lines
		while (linesIterator.hasNext()) {
			String startLine = linesIterator.next();
			if (startBrackPattern.matcher(startLine).find()) {
				buildScopeTreeHelper(startLine, linesIterator, startBrackPattern.matcher(startLine),
						endBrackPattern.matcher(startLine));

			}
		}
	}

	/**
	 * Helper function that is responsible for creating scope that starts in startBrackMatcher.
	 * @param startLine first line in scope.
	 * @param linesIterator iterator for the scope lines.
	 * @param startBrackMatcher opening bracket of the scope.
	 * @param endBrackMatcher last bracket of the scope.
	 */
	private void buildScopeTreeHelper(String startLine, ListIterator<String> linesIterator,
									  Matcher startBrackMatcher, Matcher endBrackMatcher)
			throws ScopeException {
		String firstLine = startLine;
		ArrayList<String> tempLines = new ArrayList<>();
		int numBracks = 1;
		while (numBracks > 0) {
			tempLines.add(startLine);
			linesIterator.remove();
			startLine = linesIterator.next();
			startBrackMatcher.reset(startLine);
			endBrackMatcher.reset(startLine);
			if (startBrackMatcher.find()) {
				numBracks++;
			} else if (endBrackMatcher.find()) {
				numBracks--;
			}
		}
		tempLines.add(startLine);
		createNestedScope(tempLines, this);
		linesIterator.set(firstLine.substring(0, firstLine.indexOf('{')));
	}

	/**
	 * Creates nested scope (the given scope parent's child).
	 * @param scopeCode first line in scope.
	 * @param parent parent scope.
	 */
	void createNestedScope(ArrayList<String> scopeCode, Scope parent) throws ScopeException {
		if (CONDITION_PATTERN.matcher(scopeCode.get(0)).matches() && parent != null) {
			conditionScopes.addLast(new Scope(scopeCode, this));
		} else if (METHOD_PATTERN.matcher(scopeCode.get(0)).matches()) {
			methodScopes.addLast(new Method(scopeCode, this));
		} else {
			throw new CreateScopeException(scopeCode.get(0));
		}
	}


	/**
	 * Gets a variable from a given name.
	 * @param name variable name.
	 * @return variable of given name.
	 */
	public Variable getVarFromName(String name) throws VariableException {
		if (nameToVar.get(name) != null) {
			return nameToVar.get(name);
		} else if (parentScope != null) {
			return parentScope.getVarFromName(name);
		}
		return nameToVar.get(name);
	}


	/**
	 * Adds a given variable to nameToVar hash map.
	 * @param var variable tp add.
	 */
	public void addVar(Variable var) throws SameNameException {
		if (nameToVar.containsKey(var.getName())) {
			throw new SameNameException(var);
		}
		nameToVar.put(var.getName(), var);
	}

	/**
	 * Gets conditions scopes.
	 * @return condition scopes.
	 */
	public LinkedList<Scope> getConditionScopes() {
		return conditionScopes;
	}

	/**
	 * Gets parent scope.
	 * @return parent scopes.
	 */
	public Scope getParentScope() {
		return parentScope;
	}

	/**
	 * Gets method scope.
	 * @return method scopes.
	 */
	public LinkedList<Method> getMethodScopes() {
		return methodScopes;
	}

	/**
	 * Gets scope's lines.
	 * @return scope's lines.
	 */
	public ArrayList<String> getLines() {
		return lines;
	}

}

