package parsing;

/**
 * Invalid syntax.
 * Extends ParseException.
 */
public class SyntaxException extends ParseException {
	public SyntaxException(String line) {
		super("Invalid syntax in line: \n" + line);
	}
}
