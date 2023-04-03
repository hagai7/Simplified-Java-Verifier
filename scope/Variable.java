package scope;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a variable.
 */
public class Variable {
	public static final String INT_PATTERN = "-?[0-9]+ *;?";
	public static final String CHAR_PATTERN = "'.' *;?";
	public static final String BOOLEAN_PATTERN = "( *(true|false)|(-?[0-9]+(\\.[0-9]+)?)) *;?";
	public static final String DOUBLE_PATTERN = "-?[0-9]+(\\.[0-9]+)?\\s*;?";
	public static final String STRING_PATTERN = "\".*\" *;?";
	public static final String INT = "int";
	public static final String CHAR = "char";
	public static final String BOOLEAN = "boolean";
	public static final String DOUBLE = "double";
	public static final String STRING = "String";
	private static final HashMap<String, String> typesMap = new HashMap<>();

	private final String varName;
	private final String varType;
	private String varValue;
	private boolean isConstant;

	/**
	 * This is the constructor of Variable.
	 * @param varType type of variable.
	 * @param varValue value of variable.
	 * @param varName name of variable.
	 * @param isConstant is variable final or not.
	 */
	public Variable(String varType, String varValue, String varName, boolean isConstant)
			throws AssignException {

		setTypesMap();
		this.varType = varType;
		this.varName = varName;
		this.varValue = varValue;
		this.isConstant = isConstant;
		if (isValLegal()) {
			throw new AssignException(varName);
		}
	}

	/**
	 * Sets the types HashMap that maps types to valid patterns.
	 */
	private static void setTypesMap() {
		typesMap.put(INT, INT_PATTERN);
		typesMap.put(CHAR, CHAR_PATTERN);
		typesMap.put(BOOLEAN, BOOLEAN_PATTERN);
		typesMap.put(DOUBLE, DOUBLE_PATTERN);
		typesMap.put(STRING, STRING_PATTERN);
	}

	/**
	 * Gets variable type.
	 * @return variable type.
	 */
	public String getType() {
		return varType;
	}

	/**
	 * Gets variable name.
	 * @return variable name.
	 */
	public String getName() {
		return varName;
	}

	/**
	 * Sets variable value.
	 * @param value vew value for variable.
	 */
	public void setValue(String value) throws AssignException {

		this.varValue = value;
		if (isValLegal()) {
			throw new AssignException(varName);
		}
	}


	/**
	 * Gets variable value.
	 * @return variable value.
	 */
	public String getValue() {
		return varValue;
	}

	/**
	 * Checks if the variable value is legal.
	 * @return true if legal, else false.
	 */
	private boolean isValLegal() {
		if (this.varValue == null) {
			return false;
		}
		for (Map.Entry<String, String> item : typesMap.entrySet()) {
			if (this.varType.equals(item.getKey())) {
				return !this.varValue.matches(item.getValue());
			}
		}
		return true;
	}


	/**
	 * Sets the variable as constant or a non-constant.
	 * @param isConstant the boolean value to assign.
	 */
	public void setConstant(boolean isConstant) {
		this.isConstant = isConstant;
	}

	/**
	 * Checks if the variable is constant.
	 * @return true if it is constant, else fall.
	 */
	public boolean isConstant() {
		return isConstant;
	}

}