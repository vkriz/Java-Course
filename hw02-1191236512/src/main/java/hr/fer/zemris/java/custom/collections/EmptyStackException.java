package hr.fer.zemris.java.custom.collections;

/**
 * Razred predstavlja novu vrstu iznimke koja se koristi u slučaju praznog
 * stoga u razredu StackDemo, a nasljeđuje razred RuntimeException.
 * 
 * @author Valentina Križ
 *
 */
public class EmptyStackException extends RuntimeException {
	
	/**
	 * Default konstruktor, poziva RuntimeException default konstruktor.
	 */
	public EmptyStackException() {
		super();
	}

	// zbog eclipse warning-a
	private static final long serialVersionUID = 1L;
}
