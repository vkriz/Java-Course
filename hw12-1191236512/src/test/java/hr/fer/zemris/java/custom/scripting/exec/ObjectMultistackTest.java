package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EmptyStackException;

import org.junit.jupiter.api.Test;

public class ObjectMultistackTest {
	@Test
	public void testEmptyStack() {
		ObjectMultistack multistack = new ObjectMultistack();
		
		assertTrue(multistack.isEmpty("test"));
	}
	
	@Test
	public void testPush() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push("test", new ValueWrapper(1));
		
		assertFalse(multistack.isEmpty("test"));
	}
	
	@Test
	public void testPushAndPeek() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push("test", new ValueWrapper(1));
		
		assertEquals(Integer.valueOf(1), multistack.peek("test").getValue());
		assertFalse(multistack.isEmpty("test"));
	}
	
	@Test
	public void testPushAndPop() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push("test", new ValueWrapper(1));
		
		assertEquals(Integer.valueOf(1), multistack.pop("test").getValue());
		assertTrue(multistack.isEmpty("test"));
	}
	
	@Test
	public void testPushAndDoublePop() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push("test", new ValueWrapper(1));
		
		assertEquals(Integer.valueOf(1), multistack.pop("test").getValue());
		assertThrows(EmptyStackException.class, () -> multistack.pop("test"));
	}
	
	@Test
	public void testPushAndPopAndPeek() {
		ObjectMultistack multistack = new ObjectMultistack();
		multistack.push("test", new ValueWrapper(1));
		
		assertEquals(Integer.valueOf(1), multistack.pop("test").getValue());
		assertThrows(EmptyStackException.class, () -> multistack.peek("test"));
	}
}
