package hr.fer.zemris.java.custom.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

public class SimpleHashtableTest {
	@Test
	public void emptyTable() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();

		assertEquals(0, table.size());
		assertEquals(true, table.isEmpty());
	}
	
	@Test
	public void nonEmptyTable() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(2, table.size());
	}
	
	@Test
	public void putExistingKey() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(5), "dva");
		
		assertEquals(1, table.size());
		assertEquals("dva", table.get(Integer.valueOf(5)));
	}
	
	@Test
	public void getOkayValues() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals("pet", table.get(Integer.valueOf(5)));
		assertEquals("dva", table.get(Integer.valueOf(2)));
	}
	
	@Test
	public void getNullOrNonExisting() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(null, table.get(null));
		assertEquals(null, table.get(Integer.valueOf(7)));
	}
	
	@Test
	public void containsExistingKey() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(true, table.containsKey(Integer.valueOf(5)));
	}
	
	@Test
	public void containsNonExistingKey() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(false, table.containsKey(Integer.valueOf(7)));
	}
	
	@Test
	public void containsNullKey() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), null);
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(false, table.containsKey(null));
	}
	
	@Test
	public void containsExistingValue() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(true, table.containsValue("pet"));
	}
	
	@Test
	public void containsNonExistingValue() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(false, table.containsValue("a"));
	}
	
	@Test
	public void containsNullValue() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), null);
		table.put(Integer.valueOf(2), "dva");
		
		assertEquals(true, table.containsValue(null));
	}
	
	@Test
	public void removeExistingKey() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		table.remove(Integer.valueOf(5));
		assertEquals(false, table.containsKey(Integer.valueOf(5)));
		assertEquals(1, table.size());
	}
	
	@Test
	public void removeNonExistingKey() {
		SimpleHashtable<Integer, String> table = new SimpleHashtable<>();
		table.put(Integer.valueOf(5), "pet");
		table.put(Integer.valueOf(2), "dva");
		
		table.remove(Integer.valueOf(7));
		table.remove(null);
		assertEquals(2, table.size());
	}
	
	@Test
	public void removeKeyFromTheMiddle() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);

		examMarks.remove("Jasna");
		assertEquals(3, examMarks.size());
		assertEquals(true, examMarks.containsKey("Ivana"));
		assertEquals(true, examMarks.containsKey("Ante"));
		assertEquals(true, examMarks.containsKey("Kristina"));
		assertEquals(false, examMarks.containsKey("Jasna"));
	}
	
	@Test
	public void removeKeyFromTheEnd() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);

		examMarks.remove("Kristina");
		assertEquals(3, examMarks.size());
		assertEquals(true, examMarks.containsKey("Ivana"));
		assertEquals(true, examMarks.containsKey("Ante"));
		assertEquals(true, examMarks.containsKey("Jasna"));
		assertEquals(false, examMarks.containsKey("Kristina"));
	}
	
	@Test
	public void testToString() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		assertEquals("[Ante=2, Ivana=5, Jasna=2, Kristina=5]", examMarks.toString());
	}
	
	@Test
	public void testRemoveWhileIterating() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		iter.next();
		iter.remove();
		assertEquals(true, examMarks.containsKey("Ivana"));
		assertEquals(false, examMarks.containsKey("Ante"));
		assertEquals(true, examMarks.containsKey("Jasna"));
		assertEquals(true, examMarks.containsKey("Kristina"));
	}
	
	@Test
	public void testRemoveBeforeCallingNext() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		assertThrows(IllegalStateException.class, () -> iter.remove());
	}
	
	@Test
	public void testRemoveTwoTimesInARow() {
		SimpleHashtable<String, Integer> examMarks = new SimpleHashtable<>(2);
		examMarks.put("Ivana", 2);
		examMarks.put("Ante", 2);
		examMarks.put("Jasna", 2);
		examMarks.put("Kristina", 5);
		examMarks.put("Ivana", 5);
		
		Iterator<SimpleHashtable.TableEntry<String, Integer>> iter = examMarks.iterator();
		iter.next();
		iter.remove();
		assertThrows(IllegalStateException.class, () -> iter.remove());
	}
}
