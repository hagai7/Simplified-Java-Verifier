package scope;

/**
 * Invalid condition.
 * Extends ScopeException.
 */
public class ConditionsException extends ScopeException {
	public ConditionsException() {
		super("Invalid condition");
	}
}
