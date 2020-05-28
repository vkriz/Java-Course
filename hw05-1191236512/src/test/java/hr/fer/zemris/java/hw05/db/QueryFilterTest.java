package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class QueryFilterTest {
	@Test
	public void testSimpleFilter() {
		List<ConditionalExpression> list = new ArrayList<>();
		list.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "H", ComparisonOperators.GREATER));
		
		QueryFilter filter = new QueryFilter(list);
		assertTrue(filter.accepts(new StudentRecord("0215487953", "Ivanić", "Matea", 5)));
		assertFalse(filter.accepts(new StudentRecord("9851236521", "Perić", "Ana", 2)));
	}
	
	@Test
	public void testMultipleExpressionsFilter() {
		List<ConditionalExpression> list = new ArrayList<>();
		list.add(new ConditionalExpression(FieldValueGetters.FIRST_NAME, "H", ComparisonOperators.GREATER));
		list.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "B*", ComparisonOperators.LIKE));
		
		QueryFilter filter = new QueryFilter(list);
		assertTrue(filter.accepts(new StudentRecord("0215487953", "Bulić", "Matea", 5)));
		assertFalse(filter.accepts(new StudentRecord("9851236521", "Mak", "Petar", 2)));
	}
}
