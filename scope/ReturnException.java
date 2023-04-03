package scope;

/**
 * Invalid return.
 * Extends ScopeException.
 */
public class ReturnException extends ScopeException {
	public ReturnException() {
		super("Invalid return");
	}
}
