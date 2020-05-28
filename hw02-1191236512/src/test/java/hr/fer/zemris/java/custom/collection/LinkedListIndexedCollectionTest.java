package hr.fer.zemris.java.custom.collection;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.collections.LinkedListIndexedCollection;
import hr.fer.zemris.java.custom.collections.Processor;

public class LinkedListIndexedCollectionTest {
	@Test
	public void constructor() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		assertNotNull(col);
		assertEquals(0, col.size());
	}
	
	@Test
	public void addGetSizeAndContains() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(2));
		col.add(Integer.valueOf(3));
		
		assertEquals(2, col.size());
		assertEquals(Integer.valueOf(2), col.get(0));
		assertEquals(Integer.valueOf(3), col.get(1));
		assertEquals(true, col.contains(Integer.valueOf(3)));
		assertEquals(false, col.contains(Integer.valueOf(4)));
	}
	
	@Test
	public void constructor2() {
		LinkedListIndexedCollection col1 = new LinkedListIndexedCollection();
		col1.add(Integer.valueOf(0));
		col1.add(Integer.valueOf(1));
		
		LinkedListIndexedCollection col2 = new LinkedListIndexedCollection(col1);
		
		assertNotNull(col2);
		assertEquals(2, col2.size());
		assertEquals(true, col2.contains(Integer.valueOf(0)));
		assertEquals(true, col2.contains(Integer.valueOf(1)));
	}
	
	@Test
	public void addNull() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		assertThrows(NullPointerException.class, () -> {
	        col.add(null);
	    });
	}
	
	@Test
	public void getBadIndex() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		assertThrows(IndexOutOfBoundsException.class, () -> {
	        col.get(1);
	    });
	}
	
	@Test
	public void clear() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(2));
		col.clear();
		
		assertEquals(0, col.size());
		assertEquals(false, col.contains(Integer.valueOf(2)));
	}
	
	@Test
	public void insert() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.add(Integer.valueOf(2));
		col.insert(Integer.valueOf(3), 1);
		
		assertEquals(4, col.size());
		assertEquals(Integer.valueOf(0), col.get(0));
		assertEquals(Integer.valueOf(3), col.get(1));
		assertEquals(Integer.valueOf(1), col.get(2));
		assertEquals(Integer.valueOf(2), col.get(3));
	}
	
	@Test
	public void insertNull() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		assertThrows(NullPointerException.class, () -> {
	        col.insert(null, 0);
	    });
	}
	
	@Test
	public void insertBadIndex() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		
		assertThrows(IndexOutOfBoundsException.class, () -> {
	        col.insert(Integer.valueOf(1), 1);
	    });
	}
	
	@Test
	public void indexOf() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.add(Integer.valueOf(2));
		col.insert(Integer.valueOf(3), 1);
		
		assertEquals(1, col.indexOf(Integer.valueOf(3)));
	}
	
	@Test
	public void removeIndex() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.add(Integer.valueOf(2));
		col.insert(Integer.valueOf(3), 1);
		col.remove(1);
		
		assertEquals(3, col.size());
		assertEquals(Integer.valueOf(0), col.get(0));
		assertEquals(Integer.valueOf(1), col.get(1));
		assertEquals(Integer.valueOf(2), col.get(2));
	}
	
	@Test
	public void removeObject() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.add(Integer.valueOf(2));
		col.insert(Integer.valueOf(3), 1);
		boolean b = col.remove(Integer.valueOf(3));
		
		assertEquals(3, col.size());
		assertEquals(false, col.contains(Integer.valueOf(3)));
		assertEquals(true, b);
		assertEquals(Integer.valueOf(0), col.get(0));
		assertEquals(Integer.valueOf(1), col.get(1));
		assertEquals(Integer.valueOf(2), col.get(2));
	}
	
	@Test
	public void forEach() {
		class LocalProcessor extends Processor {
			public String s;
			LocalProcessor() {
				s = new String();
			}
			public void process(Object value) {
				s = s + value.toString();
			}
		}
		
		LocalProcessor lp = new LocalProcessor();
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.forEach(lp);
		
		assertEquals(lp.s, "01");
	}
	
	@Test
	public void toArrayEmpty() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		Object[] arr = col.toArray();
		Object[] testArr = new Object[0];
		
		assertArrayEquals(arr, testArr);
	}
	
	@Test
	public void toArray() {
		LinkedListIndexedCollection col = new LinkedListIndexedCollection();
		Object[] arr = col.toArray();
		Object[] testArr = new Object[1];
		
		col.add(Integer.valueOf(1));
		arr = col.toArray();
		testArr[0] = Integer.valueOf(1);
		
		assertArrayEquals(arr, testArr);
	}
}
