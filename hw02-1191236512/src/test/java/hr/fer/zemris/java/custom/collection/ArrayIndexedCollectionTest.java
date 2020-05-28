package hr.fer.zemris.java.custom.collection;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;
import hr.fer.zemris.java.custom.collections.Processor;

public class ArrayIndexedCollectionTest {
	@Test
	public void constructor1() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		assertNotNull(col);
		assertEquals(0, col.size());
		assertEquals(16, col.capacity());
	}
	
	@Test
	public void constructor2() {
		ArrayIndexedCollection col = new ArrayIndexedCollection(12);
		
		assertNotNull(col);
		assertEquals(0, col.size());
		assertEquals(12, col.capacity());
	}
	
	@Test
	public void addGetSizeAndContains() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(Integer.valueOf(2));
		col.add(Integer.valueOf(3));
		
		assertEquals(2, col.size());
		assertEquals(Integer.valueOf(2), col.get(0));
		assertEquals(Integer.valueOf(3), col.get(1));
		assertEquals(true, col.contains(Integer.valueOf(3)));
		assertEquals(false, col.contains(Integer.valueOf(4)));
	}
	
	@Test
	public void constructor3() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		col1.add(Integer.valueOf(0));
		col1.add(Integer.valueOf(1));
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1);
		
		assertNotNull(col2);
		assertEquals(2, col2.size());
		assertEquals(16, col2.capacity());
		assertEquals(true, col2.contains(Integer.valueOf(0)));
		assertEquals(true, col2.contains(Integer.valueOf(1)));
	}
	
	@Test
	public void constructor4() {
		ArrayIndexedCollection col1 = new ArrayIndexedCollection();
		col1.add(Integer.valueOf(0));
		col1.add(Integer.valueOf(1));
		col1.add(Integer.valueOf(2));
		
		ArrayIndexedCollection col2 = new ArrayIndexedCollection(col1, 2);
		
		assertEquals(3, col2.size());
		assertEquals(3, col2.capacity());
		assertEquals(true, col2.contains(Integer.valueOf(0)));
		assertEquals(true, col2.contains(Integer.valueOf(1)));
		assertEquals(true, col2.contains(Integer.valueOf(2)));
	}
	
	@Test
	public void addNull() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		assertThrows(NullPointerException.class, () -> {
	        col.add(null);
	    });
	}
	
	@Test
	public void getBadIndex() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		assertThrows(IndexOutOfBoundsException.class, () -> {
	        col.get(1);
	    });
	}
	
	@Test
	public void clear() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(Integer.valueOf(2));
		col.clear();
		
		assertEquals(0, col.size());
		assertEquals(false, col.contains(Integer.valueOf(2)));
		assertEquals(16, col.capacity());
	}
	
	@Test
	public void insert() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
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
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		assertThrows(NullPointerException.class, () -> {
	        col.insert(null, 0);
	    });
	}
	
	@Test
	public void insertBadIndex() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		
		assertThrows(IndexOutOfBoundsException.class, () -> {
	        col.insert(Integer.valueOf(1), 1);
	    });
	}
	
	@Test
	public void indexOf() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.add(Integer.valueOf(2));
		col.insert(Integer.valueOf(3), 1);
		
		assertEquals(1, col.indexOf(Integer.valueOf(3)));
	}
	
	@Test
	public void removeIndex() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
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
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.add(Integer.valueOf(2));
		col.insert(Integer.valueOf(3), 1);
		boolean b = col.remove(Integer.valueOf(3));
		
		assertEquals(true, b);
		assertEquals(3, col.size());
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
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		col.add(Integer.valueOf(0));
		col.add(Integer.valueOf(1));
		col.forEach(lp);
		
		assertEquals(lp.s, "01");
	}
	
	@Test
	public void toArrayEmpty() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		Object[] arr = col.toArray();
		Object[] testArr = new Object[0];
		
		assertArrayEquals(arr, testArr);
	}
	
	@Test
	public void toArray() {
		ArrayIndexedCollection col = new ArrayIndexedCollection();
		Object[] arr = col.toArray();
		Object[] testArr = new Object[1];
		
		col.add(Integer.valueOf(1));
		arr = col.toArray();
		testArr[0] = Integer.valueOf(1);
		
		assertArrayEquals(arr, testArr);
	}
	
}
