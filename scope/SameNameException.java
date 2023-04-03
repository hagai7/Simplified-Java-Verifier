package scope;

/**
 * Declare a variable with a name that already exists.
 * Extends VariableException.
 */
public class SameNameException extends VariableException {

	public SameNameException(Variable var) {
		super("Variable " + var.getType() + " " + var.getName() + " already exists in this scope");
	}
}
