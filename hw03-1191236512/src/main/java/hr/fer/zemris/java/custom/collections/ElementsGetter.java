package hr.fer.zemris.java.custom.collections;

/**
 * Interface služi za kretanje kroz kolekciju.
 * 
 * @author Valentina Križ
 *
 */
public interface ElementsGetter {
	public boolean hasNextElement();
	public Object getNextElement();
	
	default public void processRemaining(Processor p) {
		while(hasNextElement()) {
			p.process(getNextElement());
		}
	}
}
