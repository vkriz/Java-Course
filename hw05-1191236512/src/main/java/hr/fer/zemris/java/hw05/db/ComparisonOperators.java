package hr.fer.zemris.java.hw05.db;

/**
 * Razred koji nudi implementacije konkretnih strategija
 * za svaki od operatora uspoređivanja.
 * 
 * @author Valentina Križ
 *
 */
public class ComparisonOperators {
	/**
	 * Implementacija operatora >.
	 */
	public static final IComparisonOperator GREATER = (value1, value2) -> {
		return value1.compareTo(value2) > 0;
	};
	
	/**
	 * Implementacija operatora >=.
	 */
	public static final IComparisonOperator GREATER_OR_EQUALS = (value1, value2) -> {
		return value1.compareTo(value2) >= 0;
	};
	
	/**
	 * Implementacija operatora <.
	 */
	public static final IComparisonOperator LESS = (value1, value2) -> {
		return !GREATER_OR_EQUALS.satisfied(value1, value2);
	};
	
	/**
	 * Implementacija operatora <=.
	 */
	public static final IComparisonOperator LESS_OR_EQUALS = (value1, value2) -> {
		return !GREATER.satisfied(value1, value2);
	};
	
	/**
	 * Implementacija operatora =.
	 */
	public static final IComparisonOperator EQUALS = (value1, value2) -> {
		return value1.compareTo(value2) == 0;
	};
	
	/**
	 * Implementacija operatora !=.
	 */
	public static final IComparisonOperator NOT_EQUALS = (value1, value2) -> {
		return !EQUALS.satisfied(value1, value2);	
	};
	
	/**
	 * Implementacija operatora LIKE.
	 */
	public static final IComparisonOperator LIKE = (value, pattern) -> {
		int count = (pattern.split("\\*", -1).length ) - 1;
		if(count > 1) {
			throw new IllegalArgumentException("Uzorak može sadržavati maksimalno jedan znak *.");
		}
		
		if(pattern.startsWith("*")) {
			return value.endsWith(pattern.substring(1));
		}
		
		if(pattern.endsWith("*")) {
			return value.startsWith(pattern.substring(0, pattern.length()-1));
		}
		
		String[] parts = pattern.split("\\*");
		
		if(parts.length == 1) {
			return EQUALS.satisfied(value, pattern);
		}

		if(value.startsWith(parts[0])) {
			return value.substring(parts[0].length()).endsWith(parts[1]);
		}
		return false;
	};
}
