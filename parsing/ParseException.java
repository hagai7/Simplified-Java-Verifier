package parsing;

/**
 * General parse exception (parent class).
 */
public class ParseException extends Throwable {
	public ParseException(String s) {
		super(s);
	}
}
