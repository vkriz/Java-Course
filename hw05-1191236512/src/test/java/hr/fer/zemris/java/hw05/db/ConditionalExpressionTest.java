package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ConditionalExpressionTest {
	@Test
	public void testSimpleQuery() {
		ConditionalExpression exp = new ConditionalExpression(FieldValueGetters.FIRST_NAME,
																"H", ComparisonOperators.GREATER);
		assertEquals(FieldValueGetters.FIRST_NAME, exp.getFieldGetter());
		assertEquals("H", exp.getStringLiteral());
		assertEquals(ComparisonOperators.GREATER, exp.getComparisonOperator());
	}
}
