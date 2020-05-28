package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;

/**
 * Razred modelira kompleksni broj s realnim koeficijentima.
 * U razredu su implementirane sve osnovne operacije s kompleksnim brojevima.
 * 
 * @author Valentina Križ
 *
 */
public class Complex {
	public static final Complex ZERO = new Complex(0,0);
	public static final Complex ONE = new Complex(1,0);
	public static final Complex ONE_NEG = new Complex(-1,0);
	public static final Complex IM = new Complex(0,1);
	public static final Complex IM_NEG = new Complex(0,-1);
	
	private double re;
	private double im;

	/**
	 * Default konstruktor, stvara broj 0.
	 */
	public Complex() {
		re = im = 0;
	}
	
	/**
	 * Konstruktor koji postavlja realni i 
	 * imaginarni dio na zadane vrijednosti.
	 * 
	 * @param re
	 * @param im
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}
	
	/**
	 * Metoda računa modul kompleksnog broja.
	 * 
	 * @return modul broja
	 */
	public double module() {
		return Math.sqrt(re*re + im*im);
	}
	
	/**
	 * Metoda za množenje dva kompleksna broja.
	 * Vraća novi kompleksni broj, oba originalna
	 * broja ostaju nepromijenjena.
	 * 
	 * @param c drugi faktor
	 * @return umnožak brojeva
	 */
	public Complex multiply(Complex c) {
		return new Complex(
				re * c.re - im * c.im, 
				re * c.im + im * c.re);
	}
	
	
	/**
	 * Metoda za dijeljenje dva kompleksna broja.
	 * Vraća novi kompleksni broj, oba originalna
	 * broja ostaju nepromijenjena.
	 * 
	 * @param c djelitelj
	 * @return 
	 */
	public Complex divide(Complex c) {
		if(c.re == 0 && c.im == 0) {
			throw new IllegalArgumentException("Dijeljenje s nulom nije dopušteno.");
		}
		
		double denominator = c.re * c.re + c.im * c.im;
		
		return new Complex(
					(re * c.re + im * c.im) / denominator,
					(im * c.re - re * c.im) / denominator);
	}
	
	/**
	 * Metoda za zbrajanje dva kompleksna broja.
	 * Vraća novi kompleksni broj, oba originalna
	 * broja ostaju nepromijenjena.
	 * 
	 * @param c drugi pribrojnik
	 * @return zbroj
	 */
	public Complex add(Complex c) {
		return new Complex(re + c.re, im + c.im);
	}
	
	/**
	 * Metoda za oduzimanje dva kompleksna broja.
	 * Vraća novi kompleksni broj, oba originalna
	 * broja ostaju nepromijenjena.
	 * 
	 * @param c drugi umanjitelj
	 * @return razlika
	 */
	public Complex sub(Complex c) {
		return new Complex(re - c.re, im - c.im);
	}
	
	/**
	 * Metoda za negiranje kompleksnog broja.
	 * Vraća novi kompleksni broj, originalni broj
	 * ostaje nepromijenjen.
	 * 
	 * @return negirani broj
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}
	
	/**
	 * Metoda za potenciranje kompleksnog broja.
	 * Vraća novi kompleksni broj, originalni broj
	 * ostaje nepromijenjen.
	 * 
	 * @param n eksponent
	 * @return potencija broja
	 * @throws IllegalArgumentException za negativan eksponent
	 */
	public Complex power(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("Metoda power prima samo nenegativne parametre.");
		}
		
		double module = Math.pow(module(), n);
		double angle = getAngle();
		
		return new Complex(module * Math.cos(n * angle), module * Math.sin(n * angle));
	}
	
	/**
	 * Metoda za računanje n-tih korijena kompleksnog broja.
	 * 
	 * @param n 
	 * @return lista n-tih korijena
	 * @throws IllegalArgumentException za negativan n
	 */
	public List<Complex> root(int n) {
		if(n <= 0) {
			throw new IllegalArgumentException("Metoda power prima samo pozitivne parametre.");
		}
		List<Complex> roots = new LinkedList<>(); 

		for(int k = 0; k < n; ++k) {
			double module = Math.pow(module(), 1.0 / n);
			double angle = (getAngle() + 2 * k * Math.PI) / n;
			
			roots.add(new Complex(module * Math.cos(n * angle), module * Math.sin(n * angle)));
		}
		
		return roots;
	}
	
	/**
	 * Metoda za pretvorbu kompleksnog broja u string.
	 * 
	 * @return string oblika re + i*im
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append(re);
		
		if(im >= 0) {
			str.append("+");
		} else {
			str.append("-");
		}
		
		str.append("i");
		str.append(Math.abs(im));
	
		return str.toString();
	}
	
	
	/**
	 * Pomoćna metoda za računanje kuta u 
	 * trigonometrijskom zapisu broja.
	 * 
	 * @return kut u radijanima
	 */
	private double getAngle() {
		// ako je broj 0, kut nije definiran
		if(re == 0 && im == 0) {
			throw new IllegalArgumentException("Za imaginarni broj 0 kut nije definiran.");
		}
				
		double angle = Math.atan(im / re);
				
		if(re >= 0) {
			if(im >= 0) {
				// prvi kvadrant
				return angle;
			} else {
				// četvrti kvadrant
				return angle + 2 * Math.PI;
			}
		} else {
			// drugi ili treći kvadrant
			return angle + Math.PI;
		}
	}

}
