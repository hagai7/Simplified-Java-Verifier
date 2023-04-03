package scope;

/**
 * Invalid call or declaration.
 * Extends ScopeException.
 */
public class MethodException extends ScopeException {
	public MethodException(String methodName) {
		super("Invalid call or declaration of method " +methodName);
	}
}
