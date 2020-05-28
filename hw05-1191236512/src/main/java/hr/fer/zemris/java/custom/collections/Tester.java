package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje služi za modeliranje objekata koji prime
 * neki objekt te ispitaju je li taj objekt prihvatljiv ili nije.
 * 
 * @author Valentina Križ
 *
 */
public interface Tester<T> {
	boolean test(T obj);
}
