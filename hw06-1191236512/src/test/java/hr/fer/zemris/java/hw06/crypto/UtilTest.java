package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UtilTest {
	@Test
	public void testHexToByte() {
		byte[] bytes = Util.hextobyte("01aE22");
		
		assertEquals(1, bytes[0]);
		assertEquals(-82, bytes[1]);
		assertEquals(34, bytes[2]);
	}
	
	@Test
	public void testHexToByteEmptyString() {
		assertEquals(0, Util.hextobyte("").length);
	}
	
	@Test
	public void testHexToByteUnevenLength() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("aaa"));
	}
	
	@Test
	public void testHexToByteInvalidChar() {
		assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("15k7"));
	}
	
	@Test
	public void testByteToHex() {
		assertEquals("01ae22", Util.bytetohex(new byte[] {1, -82, 34}));
	}
	
	@Test
	public void testByteToHexEmptyArray() {
		assertEquals(0, Util.bytetohex(new byte[] {}).length());
	}
}
