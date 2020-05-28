package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QueryParserTest {
	@Test
	public void testDirectQuery() {
		QueryParser qp = new QueryParser(" jmbag =\"0123456789\" ");
		
		assertTrue(qp.isDirectQuery());
		assertEquals("0123456789", qp.getQueriedJMBAG());
		assertEquals(1, qp.getQuery().size());
	}
	
	@Test
	public void testQueryWithLogicalAnd() {
		QueryParser qp = new QueryParser("jmbag=\"0123456789\" and lastName>\"J\"");
		
		assertFalse(qp.isDirectQuery());
		assertThrows(IllegalStateException.class, () -> qp.getQueriedJMBAG());
		assertEquals(2, qp.getQuery().size());
	}
	
	@Test
	public void testQueryStartsWithLogicalAnd() {
		assertThrows(QueryParserException.class, () -> new QueryParser("and jmbag=\"0123456789\" and lastName>\"J\""));
	}
	
	@Test
	public void testInvalidAttributeName() {
		assertThrows(QueryParserException.class, () -> new QueryParser("finalGrade=\"5\" and lastName>\"J\""));
	}
}
