package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class StudentDatabaseTest {
	@Test
	public void testFilterAlwaysFalse() {
		List<String> list = new ArrayList<>();
		list.add("0000000001	Akšamović	Marin	2");
		list.add("0000000003	Bosnić	Andrea	4");
		StudentDatabase db = new StudentDatabase(list);
		
		assertTrue(db.filter(rec -> {return false;}).isEmpty());
	}
	
	@Test
	public void testFilterAlwaysTrue() {
		List<String> list = new ArrayList<>();
		list.add("0000000001	Akšamović	Marin	2");
		list.add("0000000003	Bosnić	Andrea	4");
		StudentDatabase db = new StudentDatabase(list);
		
		assertEquals(2, db.filter(rec -> {return true;}).size());
	}
	
	@Test
	public void testDoubleJMBAG() {
		List<String> list = new ArrayList<>();
		list.add("0000000001	Akšamović	Marin	2");
		list.add("0000000001	Bosnić	Andrea	4");
		
		assertThrows(Exception.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testInvalidGrade() {
		List<String> list = new ArrayList<>();
		list.add("0000000001	Akšamović	Marin	2");
		list.add("0000000003	Bosnić	Andrea	7");
		
		assertThrows(Exception.class, () -> new StudentDatabase(list));
	}
	
	@Test
	public void testForExistingJMBAG() {
		List<String> list = new ArrayList<>();
		list.add("0000000001	Akšamović	Marin	2");
		list.add("0000000003	Bosnić	Andrea	4");
		
		StudentDatabase db = new StudentDatabase(list);
		assertEquals("Andrea", db.forJMBAG("0000000003").getFirstName());
	}
	
	@Test
	public void testForNonExistingJMBAG() {
		List<String> list = new ArrayList<>();
		list.add("0000000001	Akšamović	Marin	2");
		list.add("0000000003	Bosnić	Andrea	4");
		
		StudentDatabase db = new StudentDatabase(list);
		assertNull(db.forJMBAG("009"));
	}
}
