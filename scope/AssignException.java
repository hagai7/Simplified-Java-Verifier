package scope;

/**
 * Invalid assignment to a variable.
 * Extends VariableException.
 */
public class AssignException extends VariableException {
	public AssignException(String var) {
		super("Invalid assignment in variable: " + var);
	}
}
