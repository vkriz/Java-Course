package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ComparisonOperatorsTest {
	@Test
	public void testEqualStrings() {
		String s1 = "first";
		String s2 = "first";
		
		assertTrue(ComparisonOperators.EQUALS.satisfied(s1, s2));
		assertFalse(ComparisonOperators.NOT_EQUALS.satisfied(s1, s2));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(s1, s2));
		assertFalse(ComparisonOperators.GREATER.satisfied(s1, s2));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(s1, s2));
		assertFalse(ComparisonOperators.LESS.satisfied(s1, s2));
	}
	
	@Test
	public void testFirstStringIsGreater() {
		String s1 = "second";
		String s2 = "first";
		
		assertFalse(ComparisonOperators.EQUALS.satisfied(s1, s2));
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied(s1, s2));
		assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied(s1, s2));
		assertTrue(ComparisonOperators.GREATER.satisfied(s1, s2));
		assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied(s1, s2));
		assertFalse(ComparisonOperators.LESS.satisfied(s1, s2));
	}
	
	@Test
	public void testFirstStringIsLess() {
		String s1 = "Ana";
		String s2 = "Jasna";
		
		assertFalse(ComparisonOperators.EQUALS.satisfied(s1, s2));
		assertTrue(ComparisonOperators.NOT_EQUALS.satisfied(s1, s2));
		assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied(s1, s2));
		assertFalse(ComparisonOperators.GREATER.satisfied(s1, s2));
		assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied(s1, s2));
		assertTrue(ComparisonOperators.LESS.satisfied(s1, s2));
	}
	
	@Test
	public void testStringIsLike() {
		assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Bakamović", "B*"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Bosnić", "B*ić"));
		assertTrue(ComparisonOperators.LIKE.satisfied("Ružić", "*ić"));
	}
	
	@Test
	public void testStringIsNotLike() {
		assertFalse(ComparisonOperators.LIKE.satisfied("Zagreb", "Aba*"));
		assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"));
		assertFalse(ComparisonOperators.LIKE.satisfied("Bosnić", "B*i"));
		assertThrows(Exception.class, () -> ComparisonOperators.LIKE.satisfied("Bosnić", "B*i*"));
	}
}
