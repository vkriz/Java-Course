package hr.fer.zemris.java.custom.collections;

/**
 * Predstavlja razred koji je u mogućnosti raditi neku operaciju na
 * proslijeđenom objektu.
 * 
 * @author Valentina Križ
 * 
 * @param <T> tip objekata nad kojima procesor radi
 *
 */
public interface Processor<T> {
	/**
	 * Metoda koja obavlja operaciju za koju je procesor zadužen.
	 * 
	 * @param value objekt nad kojim se obavlja operacija
	 */
	void process(T value);
}
