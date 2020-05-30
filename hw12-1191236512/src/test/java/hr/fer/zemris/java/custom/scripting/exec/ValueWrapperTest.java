package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ValueWrapperTest {
	@Test
	public void testIntegerAndInteger() {
		ValueWrapper first = new ValueWrapper(3);
		ValueWrapper second = new ValueWrapper(3);
		first.add(second);
		
		assertTrue(first.getValue() instanceof Integer);
	}
	
	@Test
	public void testIntegerAndDouble() {
		ValueWrapper first = new ValueWrapper(3);
		ValueWrapper second = new ValueWrapper(3.14);
		first.subtract(second);
		
		assertTrue(first.getValue() instanceof Double);
	}
	
	@Test
	public void testIntegerAndNull() {
		ValueWrapper first = new ValueWrapper(3);
		ValueWrapper second = new ValueWrapper(null);
		first.multiply(second);
		
		assertTrue(first.getValue() instanceof Integer);
	}
	
	@Test
	public void testDoubleAndNull() {
		ValueWrapper first = new ValueWrapper(3.14);
		ValueWrapper second = new ValueWrapper(null);
		first.add(second);
		
		assertTrue(first.getValue() instanceof Double);
	}
	
	@Test
	public void testIntegerAndStringInteger() {
		ValueWrapper first = new ValueWrapper(3);
		ValueWrapper second = new ValueWrapper("3");
		first.divide(second);
		
		assertTrue(first.getValue() instanceof Integer);
	}
	
	@Test
	public void testIntegerAndStringDouble() {
		ValueWrapper first = new ValueWrapper(3);
		ValueWrapper second = new ValueWrapper("3.14");
		first.divide(second);
		
		assertTrue(first.getValue() instanceof Double);
	}
	
	@Test
	public void testDoubleAndStringInteger() {
		ValueWrapper first = new ValueWrapper(3.14);
		ValueWrapper second = new ValueWrapper("3");
		first.subtract(second);
		
		assertTrue(first.getValue() instanceof Double);
	}
	
	@Test
	public void testDoubleAndStringDouble() {
		ValueWrapper first = new ValueWrapper(3.14);
		ValueWrapper second = new ValueWrapper("3.14");
		first.multiply(second);
		
		assertTrue(first.getValue() instanceof Double);
	}
	
	@Test
	public void testNonNumericalString() {
		ValueWrapper first = new ValueWrapper("bla");
		ValueWrapper second = new ValueWrapper(3);
		
		assertThrows(RuntimeException.class, () -> first.add(second));
	}
	
	@Test
	public void testUnsupportedType() {
		ValueWrapper first = new ValueWrapper(Boolean.valueOf(true));
		ValueWrapper second = new ValueWrapper(3);
		
		assertThrows(RuntimeException.class, () -> first.add(second));
	}
	
	@Test
	public void testDivisionWithZero() {
		ValueWrapper first = new ValueWrapper(2);
		ValueWrapper second = new ValueWrapper(0);
		
		assertThrows(IllegalArgumentException.class, () -> first.divide(second));
	}
	
	@Test
	public void testDivisionWithNull() {
		ValueWrapper first = new ValueWrapper(2);
		ValueWrapper second = new ValueWrapper(null);
		
		assertThrows(IllegalArgumentException.class, () -> first.divide(second));
	}
	
	@Test
	public void testComparissonEqualIntegers() {
		ValueWrapper first = new ValueWrapper(0);
		ValueWrapper second = new ValueWrapper(null);
		
		assertEquals(0, first.numCompare(second));
	}
	
	@Test
	public void testComparissonEqualDoubleAndInteger() {
		ValueWrapper first = new ValueWrapper(0);
		ValueWrapper second = new ValueWrapper("0.0");
		
		assertEquals(0, first.numCompare(second));
	}
	
	@Test
	public void testComparissonLessDoubleAndInteger() {
		ValueWrapper first = new ValueWrapper(-1);
		ValueWrapper second = new ValueWrapper("0.0");
		
		assertTrue(first.numCompare(second) < 0);
	}
	
	@Test
	public void testComparissonGreaterStringAndInteger() {
		ValueWrapper first = new ValueWrapper("2.4");
		ValueWrapper second = new ValueWrapper(2);
		
		assertTrue(first.numCompare(second) > 0);
	}
}
