package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FieldValueGettersTest {
	@Test
	public void testGetters() {
		StudentRecord record = new StudentRecord("1191236512", "Križ", "Valentina", 2);
		
		assertEquals("1191236512", FieldValueGetters.JMBAG.get(record));
		assertEquals("Križ", FieldValueGetters.LAST_NAME.get(record));
		assertEquals("Valentina", FieldValueGetters.FIRST_NAME.get(record));
	}
}
