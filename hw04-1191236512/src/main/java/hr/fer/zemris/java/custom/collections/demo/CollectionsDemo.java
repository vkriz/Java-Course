package hr.fer.zemris.java.custom.collections.demo;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.ElementsGetter;
import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.List;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Razred za potrebe demonstracije rada parametriziranih kolekcija.
 * 
 * @author Valentina Križ
 *
 */
public class CollectionsDemo {
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String args[]) {
		// ArrayIndexedCollection
		ArrayIndexedCollection<String> arr = new ArrayIndexedCollection<>();
		arr.add("prvi");
		// arr.add(1); kompajler se buni
		try {
			arr.add(null);
		} catch (NullPointerException ex) {
			System.out.println(ex.getMessage()); // U kolekciju nije dopušteno ubacivanje null referenci.
		}
		
		System.out.println(arr.contains(1)); // false
		System.out.println(arr.contains(null)); // false
		System.out.println(arr.contains("prvi")); // true
		
		String first = arr.get(0);
		System.out.println(first); // "prvi"
		
		ElementsGetter<String> getter = arr.createElementsGetter();
		System.out.println(getter.hasNextElement()); // true
		System.out.println(getter.getNextElement()); // "prvi"
		
		// ObjectStack
		ObjectStack<List<String>> stack = new ObjectStack<>();
		LinkedListIndexedCollection<String> l1 = new LinkedListIndexedCollection<>();
		l1.add("prvi");
		l1.add("drugi");
		
		LinkedListIndexedCollection<String> l2 = new LinkedListIndexedCollection<>();
		l2.add("treći");
		l2.add("četvrti");
		
		stack.push(l1);
		stack.push(l2);
		
		List<String> testList = stack.peek();
		System.out.println(testList.get(0)); // "treći"
	}
}
