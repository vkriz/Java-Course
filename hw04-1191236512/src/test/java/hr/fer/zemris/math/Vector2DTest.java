package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class Vector2DTest {
	private static final double epsilon = 1e-6;
	
	@Test
	public void testZeroVector() {
		Vector2D vec = new Vector2D(0, 0);
		
		assertEquals(0, vec.getX());
		assertEquals(0, vec.getY());
	}
	
	@Test
	public void testCopyVector() {
		Vector2D vec = new Vector2D(0, 0);
		Vector2D copyVec = vec.copy();
		
		assertEquals(0, copyVec.getX());
		assertEquals(0, copyVec.getY());
	}
	
	@Test
	public void translation() {
		Vector2D vec = new Vector2D(0, 0);
		Vector2D offset = new Vector2D(5, 7);
		vec.translate(offset);
		
		assertEquals(5, vec.getX());
		assertEquals(7, vec.getY());
	}
	
	@Test
	public void translatedVector() {
		Vector2D vec = new Vector2D(0, 0);
		Vector2D offset = new Vector2D(5, 7);
		Vector2D translatedVec = vec.translated(offset);
		
		assertEquals(5, translatedVec.getX());
		assertEquals(7, translatedVec.getY());
	}
	
	@Test
	public void rotationBy0() {
		Vector2D vec = new Vector2D(1, 1);
		vec.rotate(0);

		assertEquals(1, vec.getX(), epsilon);
		assertEquals(1, vec.getY(), epsilon);
	}
	
	@Test
	public void rotationByPi() {
		Vector2D vec = new Vector2D(1, 1);
		vec.rotate(Math.PI / 2);

		assertEquals(-1, vec.getX(), epsilon);
		assertEquals(1, vec.getY(), epsilon);
	}
	
	@Test
	public void rotationByPiHalf() {
		Vector2D vec = new Vector2D(1, 1);
		vec.rotate(Math.PI / 2);
		
		assertEquals(-1, vec.getX(), epsilon);
		assertEquals(1, vec.getY(), epsilon);
	}
	
	@Test
	public void rotatedVector() {
		Vector2D vec = new Vector2D(2, 1);
		Vector2D rotatedVec = vec.rotated(3 * Math.PI / 2);
	
		assertEquals(1, rotatedVec.getX(), epsilon);
		assertEquals(-2, rotatedVec.getY(), epsilon);
	}
	
	@Test
	public void scaling() {
		Vector2D vec = new Vector2D(2, 2);
		vec.scale(0.5);
		
		assertEquals(1, vec.getX(), epsilon);
		assertEquals(1, vec.getY(), epsilon);
	}
	
	@Test
	public void scaledVector() {
		Vector2D vec = new Vector2D(1, 1);
		Vector2D scaledVector = vec.scaled(2);
		
		assertEquals(2, scaledVector.getX(), epsilon);
		assertEquals(2, scaledVector.getY(), epsilon);
	}
}
