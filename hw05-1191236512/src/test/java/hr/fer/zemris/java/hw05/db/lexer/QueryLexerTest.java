package hr.fer.zemris.java.hw05.db.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class QueryLexerTest {
	private void checkTokenStream(QueryLexer lexer, QueryToken[] correctData) {
		int counter = 0;
		for(QueryToken expected : correctData) {
			QueryToken actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}
	
	@Test
	public void testNotNull() {
		QueryLexer lexer = new QueryLexer("");
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testEmpty() {
		QueryLexer lexer = new QueryLexer("");
		
		assertEquals(QueryTokenType.EOL, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}
	
	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		QueryLexer lexer = new QueryLexer("");
		
		QueryToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	@Test
	public void testRadAfterEOL() {
		QueryLexer lexer = new QueryLexer("");
		
		// will obtain EOL
		lexer.nextToken();
		// will throw!
		assertThrows(QueryLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void testSimpleQuery() {
		QueryLexer lexer = new QueryLexer("jmbag=\"0000000003\"");
		
		QueryToken correctData[] = {
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "jmbag"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "EQUALS"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "0000000003"),
				new QueryToken(QueryTokenType.EOL, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testSimpleQueryWithWhitespaces() {
		QueryLexer lexer = new QueryLexer(" lastName = \"Blažić\"");
		
		QueryToken correctData[] = {
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "lastName"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "EQUALS"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "Blažić"),
				new QueryToken(QueryTokenType.EOL, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testWithLogicalAnd() {
		QueryLexer lexer = new QueryLexer("firstName>\"A\" and lastName LIKE \"B*ć\"\r\n");
		
		QueryToken correctData[] = {
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "firstName"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "GREATER"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "A"),
				new QueryToken(QueryTokenType.AND, null),
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "lastName"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "LIKE"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "B*ć"),
				new QueryToken(QueryTokenType.EOL, null)
		};

		checkTokenStream(lexer, correctData);
	}

	@Test
	public void testWithMultipleLogicalAnds() {
		QueryLexer lexer = new QueryLexer("firstName>\"A\" and firstName<\"C\" and lastName LIKE \"B*ć\" and jmbag>\"0000000002\"");
		
		QueryToken correctData[] = {
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "firstName"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "GREATER"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "A"),
				new QueryToken(QueryTokenType.AND, null),
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "firstName"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "LESS"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "C"),
				new QueryToken(QueryTokenType.AND, null),
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "lastName"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "LIKE"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "B*ć"),
				new QueryToken(QueryTokenType.AND, null),
				new QueryToken(QueryTokenType.ATTRIBUTE_NAME, "jmbag"),
				new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "GREATER"),
				new QueryToken(QueryTokenType.STRING_LITERAL, "0000000002"),
				new QueryToken(QueryTokenType.EOL, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void testClosingQuoteMissing() {
		QueryLexer lexer = new QueryLexer(" lastName = \"Blažić");
		
		assertEquals(QueryTokenType.ATTRIBUTE_NAME, lexer.nextToken().getType());
		assertEquals(QueryTokenType.COMPARISON_OPERATOR, lexer.nextToken().getType());
		assertThrows(QueryLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void testUnsupportedOperator() {
		QueryLexer lexer = new QueryLexer(" lastName ! \"Blažić\"");
		
		assertEquals(QueryTokenType.ATTRIBUTE_NAME, lexer.nextToken().getType());
		assertThrows(QueryLexerException.class, () -> lexer.nextToken());
	}
}
