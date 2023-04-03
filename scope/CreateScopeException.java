package scope;

/**
 * Invalid start of scope so fail to create new scope.
 * Extends ScopeException.
 */
public class CreateScopeException extends ScopeException {

	public CreateScopeException(String s) {
		super("Invalid start of scope in " + s + " so failed to create new scope.");
	}
}
