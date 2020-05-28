package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

public class SmartScriptLexerTest {
	private void checkTokenStream(SmartScriptLexer lexer, SmartScriptToken[] correctData) {
		int counter = 0;
		for(SmartScriptToken expected : correctData) {
			SmartScriptToken actual = lexer.nextToken();
			String msg = "Checking token "+counter + ":";
			assertEquals(expected.getType(), actual.getType(), msg);
			assertEquals(expected.getValue(), actual.getValue(), msg);
			counter++;
		}
	}
	
	@Test
	public void testNullState() {
		assertThrows(NullPointerException.class, () -> new SmartScriptLexer("").setState(null));
	}
	
	@Test
	public void testNotNull() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
	}

	@Test
	public void testEmpty() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertEquals(SmartScriptTokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
	}

	@Test
	public void testGetReturnsLastNext() {
		// Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		SmartScriptToken token = lexer.nextToken();
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
		assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
	}

	@Test
	public void testRadAfterEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		// will obtain EOF
		lexer.nextToken();
		// will throw!
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void badTagName() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$_KL$}");
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void okayTagName() {
		SmartScriptLexer lexer = new SmartScriptLexer("{$for_$}");
		
		SmartScriptToken correctData[] = {
				new SmartScriptToken(SmartScriptTokenType.TAGNAME, "FOR_"),
				new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
				new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void unsupportedEscape() {
		SmartScriptLexer lexer = new SmartScriptLexer("\\\"");
		assertThrows(SmartScriptLexerException.class, () -> lexer.nextToken());
	}
	
	@Test
	public void forLoopThreeParameters() {
		String document = "{$ FOR i -1 10 1 $}{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "FOR"),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, -1),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, 10),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, 1),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "END"),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void forLoopFourParameters() {
		String document = "{$ FOR i -1 10 1 1.1$}{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "FOR"),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, -1),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, 10),
			new SmartScriptToken(SmartScriptTokenType.INTEGER, 1),
			new SmartScriptToken(SmartScriptTokenType.DOUBLE, 1.1),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "END"),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void forLoopFourParametersNoWhitespace() {
		String document = "{$ FOR i-1.35bbb\"1\" $}{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "FOR"),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
			new SmartScriptToken(SmartScriptTokenType.DOUBLE, -1.35),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, "bbb"),
			new SmartScriptToken(SmartScriptTokenType.STRING, "1"),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "END"),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void forLoopFourParametersWithWhitespaces() {
		String document = "{$ FOR i -1.35 bbb \"1\" $}{$END$}";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "FOR"),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, "i"),
			new SmartScriptToken(SmartScriptTokenType.DOUBLE, -1.35),
			new SmartScriptToken(SmartScriptTokenType.VARIABLE, "bbb"),
			new SmartScriptToken(SmartScriptTokenType.STRING, "1"),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.TAGNAME, "END"),
			new SmartScriptToken(SmartScriptTokenType.TAGCLOSING, null),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	@Test
	public void textOnly() {
		String document = "Example { bla } blu \\{$=1$}. Nothing interesting {=here}.";
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		SmartScriptToken correctData[] = {
			new SmartScriptToken(SmartScriptTokenType.TEXT, "Example { bla } blu \\{$=1$}. Nothing interesting {=here}."),
			new SmartScriptToken(SmartScriptTokenType.EOF, null)
		};

		checkTokenStream(lexer, correctData);
	}
	
	private String loader(String filename) {
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
		    if(is==null) throw new RuntimeException("Datoteka "+filename+" je nedostupna.");
		    byte[] data = this.getClass().getClassLoader().getResourceAsStream(filename).readAllBytes();
		    String text = new String(data, StandardCharsets.UTF_8);
		    return text;
		  } catch(IOException ex) {
		    throw new RuntimeException("Greška pri čitanju datoteke.", ex);
		  }
	}
	
	@Test
	public void testExampleFromHomework() {
		String document = loader("primjer1.txt");
		SmartScriptLexer lexer = new SmartScriptLexer(document);
		
		assertEquals(SmartScriptTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TAGNAME, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.INTEGER, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.INTEGER, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.INTEGER, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TAGCLOSING, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TAGNAME, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.VARIABLE, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TAGCLOSING, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TEXT, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TAGNAME, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TAGCLOSING, lexer.nextToken().getType());
		assertEquals(SmartScriptTokenType.TEXT, lexer.nextToken().getType());
	}
}
