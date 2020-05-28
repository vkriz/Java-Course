package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Vector3Test {
	@Test
	public void testConstructorAndGetters() {
		Vector3 i = new Vector3(1,0,0);
		
		assertEquals(1, i.getX());
		assertEquals(0, i.getY());
		assertEquals(0, i.getZ());
	}
}
