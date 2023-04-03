package parsing;

/**
 * Invalid number of arguments in calling method.
 * Extends ParseException.
 */
public class NumArgsException extends ParseException {
	public NumArgsException() {
		super("number of arguments in method call differ from signature");
	}
}
