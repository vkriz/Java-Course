package hr.fer.zemris.java.hw02;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ComplexNumberTest {

	// varijabla za usporedivanje (assertEquals) double brojeva 
	private static final double epsilon = 1e-6;
	
	@Test
	public void constructor() {
		ComplexNumber num = new ComplexNumber(-4, 3);
		
		assertNotNull(num);
	}
	
	@Test
	public void realPart() {
		ComplexNumber num = new ComplexNumber(-4, 3);
		
		assertEquals(-4, num.getReal(), epsilon);
	}
	
	@Test
	public void imaginaryPart() {
		ComplexNumber num = new ComplexNumber(-4, 3);
		
		assertEquals(3, num.getImaginary(), epsilon);
	}
	
	@Test
	public void fromReal() {
		ComplexNumber num = ComplexNumber.fromReal(-4);
		
		assertEquals(-4, num.getReal(), epsilon);
		assertEquals(0, num.getImaginary(), epsilon);
	}
	
	@Test
	public void fromImaginary() {
		ComplexNumber num = ComplexNumber.fromImaginary(3);
		
		assertEquals(0, num.getReal(), epsilon);
		assertEquals(3, num.getImaginary(), epsilon);
	}
	
	@Test
	public void fromMagnitudeAndAngle() {
		ComplexNumber num = ComplexNumber.fromMagnitudeAndAngle(3, 0.7853981633974483);
		
		assertEquals(2.1213203435596424, num.getReal(), epsilon);
		assertEquals(2.1213203435596424, num.getImaginary(), epsilon);
	}
	
	@Test
	public void magnitude() {
		ComplexNumber num = new ComplexNumber(-4, 3);
		
		assertEquals(5, num.getMagnitude(), epsilon);
	}

	@Test
	public void angle() {
		ComplexNumber num = new ComplexNumber(-4, 3);
		
		assertEquals(2.498091544796509, num.getAngle(), epsilon);
	}
	
	@Test
	public void angleOfZero() {
		ComplexNumber num = new ComplexNumber(0, 0);
		
		assertThrows(Exception.class, () -> {
			num.getAngle();
	    });
	}
	
	@Test
	public void addition() {
		ComplexNumber num1 = new ComplexNumber(2, 3.5);
		ComplexNumber num2 = new ComplexNumber(3.2, -1.5);
		
		ComplexNumber res = num1.add(num2);
		assertEquals(5.2, res.getReal(), epsilon);
		assertEquals(2, res.getImaginary(), epsilon);
	}
	
	@Test
	public void subtraction() {
		ComplexNumber num1 = new ComplexNumber(2, 3.5);
		ComplexNumber num2 = new ComplexNumber(3.2, -1.5);
		
		ComplexNumber res = num1.sub(num2);
		assertEquals(-1.2, res.getReal(), epsilon);
		assertEquals(5, res.getImaginary(), epsilon);
	}
	
	@Test
	public void multiplication() {
		ComplexNumber num1 = new ComplexNumber(2.5, 4);
		ComplexNumber num2 = new ComplexNumber(3, -8.12);
		
		ComplexNumber res = num1.mul(num2);
		assertEquals(39.98, res.getReal(), epsilon);
		assertEquals(-8.3, res.getImaginary(), epsilon);
	}
	
	@Test
	public void division() {
		ComplexNumber num1 = new ComplexNumber(2.5, 4);
		ComplexNumber num2 = new ComplexNumber(3, -8.12);
		
		ComplexNumber res = num1.div(num2);
		assertEquals(-0.333358244, res.getReal(), epsilon);
		assertEquals(0.431043686, res.getImaginary(), epsilon);
	}
	

	@Test
	public void power() {
		ComplexNumber num = new ComplexNumber(3, -8.12);
		
		ComplexNumber res = num.power(3);
		assertEquals(-566.4096, res.getReal(), epsilon);
		assertEquals(316.147328, res.getImaginary(), epsilon);
	}
	
	@Test
	public void root() {
		ComplexNumber num = new ComplexNumber(3, -8.12);
		
		ComplexNumber[] res = num.root(3);
		assertEquals(-0.241658654727002, res[0].getReal(), epsilon);
		assertEquals(2.03900331658754, res[0].getImaginary(), epsilon);
		assertEquals(-1.64499934320203, res[1].getReal(), epsilon);
		assertEquals(-1.22878419233173, res[1].getImaginary(), epsilon);
		assertEquals(1.88665799792904, res[2].getReal(), epsilon);
		assertEquals(-0.810219124255817, res[2].getImaginary(), epsilon);
	}
	
	@Test
	public void parseNormal() {
		ComplexNumber num = ComplexNumber.parse("2.13+5i");
		
		assertEquals(2.13, num.getReal(), epsilon);
		assertEquals(5, num.getImaginary(), epsilon);
		
	}
	
	@Test
	public void parseOnlyImaginary() {
		ComplexNumber num = ComplexNumber.parse("- i");

		assertEquals(0, num.getReal(), epsilon);
		assertEquals(-1, num.getImaginary(), epsilon);
	}
	
	
	@Test
	public void parseOnlyReal() {
		ComplexNumber num = ComplexNumber.parse("6.13 ");
		
		assertEquals(6.13, num.getReal(), epsilon);
		assertEquals(0, num.getImaginary(), epsilon);
	}
	
	@Test
	public void parseMultipleSigns() {
		assertThrows(NumberFormatException.class, () -> {
			ComplexNumber.parse("6.13 -+ 4i ");
	    });
	}
	
	@Test
	public void parseBadInput() {
		assertThrows(NumberFormatException.class, () -> {
			ComplexNumber.parse("6.13 kl 4i ");
	    });
	}
	
	@Test
	public void stringNormal() {
		ComplexNumber num1 = new ComplexNumber(2.1, 3.5);
		
		String s = num1.toString();
		assertEquals(s, "2.1+3.5i");
	}
	
	@Test
	public void stringOnlyComplex() {
		ComplexNumber num1 = new ComplexNumber(0, 3.5);
		
		String s = num1.toString();
		assertEquals(s, "3.5i");
	}
	
	@Test
	public void stringZero() {
		ComplexNumber num1 = new ComplexNumber(0, 0);
		
		String s = num1.toString();
		assertEquals(s, "");
	}
}
