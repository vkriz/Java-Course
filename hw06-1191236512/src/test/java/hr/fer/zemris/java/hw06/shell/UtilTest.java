package hr.fer.zemris.java.hw06.shell;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UtilTest {
	@Test
	public void testRemoveQuotes() {
		assertEquals("test.txt", Util.removeQuotes("\"test.txt\""));
	}
	
	@Test
	public void testRemoveQuotesNoQuotes() {
		assertEquals("test.txt", Util.removeQuotes("test.txt"));
	}
	
	@Test
	public void testGetNextArgumentOneArgument() {
		assertEquals("test.txt", Util.getNextArgument("test.txt", true, 0));
	}
	
	@Test
	public void testGetNextArgumentTwoArguments() {
		assertEquals("test.txt", Util.getNextArgument("test.txt test2.txt", true, 0));
		assertEquals("test2.txt", Util.getNextArgument("test.txt test2.txt", true, "test.txt".length()));
	}
	
	@Test
	public void testGetNextArgumentTwoArgumentsWithQuotes() {
		assertEquals("test.txt", Util.getNextArgument("test.txt test2.txt", true, 0));
		assertEquals("\"test2.txt\"", Util.getNextArgument("test.txt \"test2.txt\"", true, "test.txt".length()));
	}
	
	@Test
	public void testGetNextArgumentTwoArgumentsBothWithQuotes() {
		assertEquals("\"test.txt\"", Util.getNextArgument("\"test.txt\" \"test2.txt\"", true, 0));
		assertEquals("\"test2.txt\"", Util.getNextArgument("\"test.txt\" \"test2.txt\"", true, "\"test.txt\"".length()));
	}
	
	@Test
	public void testGetNextArgumentQuotesNotAllowed() {
		assertEquals("\"test.txt\"", Util.getNextArgument("\"test.txt\" \"test2.txt\"", true, 0));
		assertThrows(IllegalArgumentException.class, () -> Util.getNextArgument("\"test.txt\" \"test2.txt\"", false, "\"test.txt\"".length()));
	}
}
