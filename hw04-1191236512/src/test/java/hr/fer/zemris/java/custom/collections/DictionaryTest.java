package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DictionaryTest {
	@Test
	public void testEmpty() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		
		assertEquals(true, dict.isEmpty());
	}
	
	@Test
	public void testPutNew() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(Integer.valueOf(0), "prvi");
		
		assertEquals(false, dict.isEmpty());
		assertEquals(1, dict.size());
		assertEquals("prvi", dict.get(Integer.valueOf(0)));
	}
	
	@Test
	public void testPutExisting() {
		Dictionary<Integer, String> dict = new Dictionary<>();
		dict.put(Integer.valueOf(0), "prvi");
		dict.put(Integer.valueOf(0), "drugi");
		
		assertEquals(false, dict.isEmpty());
		assertEquals(1, dict.size());
		assertEquals("drugi", dict.get(Integer.valueOf(0)));
	}
}
