package hr.fer.zemris.java.custom.collections;

/**
 * Interface služi za kretanje kroz kolekciju.
 * 
 * @author Valentina Križ
 *
 * @param <T> tip objekata u kolekciji
 */
public interface ElementsGetter<T> {
	public boolean hasNextElement();
	public T getNextElement();
	
	default public void processRemaining(Processor<T> p) {
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
