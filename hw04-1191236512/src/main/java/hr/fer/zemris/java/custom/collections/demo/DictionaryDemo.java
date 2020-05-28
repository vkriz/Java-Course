package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.Dictionary;

/**
 * Razred za potrebe demonstracije rada razreda Dictionary.
 * 
 * @author Valentina Križ
 *
 */
public class DictionaryDemo {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String args[]) {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		dict.put(1, "prvi");
		dict.put(2, "drugi");
		System.out.println(dict.get(1)); // "prvi"
		System.out.println(dict.get(2)); // "drugi"
		System.out.println(dict.get(3)); // null
		dict.clear();
		System.out.println(dict.size()); // 0
		
		dict.put(1, "prvi");
		dict.put(1, "prvi2");
		System.out.println(dict.get(1)); // "prvi2"
	}
}
