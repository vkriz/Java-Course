package hr.fer.zemris.java.hw02;

/**
 * Razred koji predstavlja i omogućuje rad s kompleksnim brojevima.
 * U razredu su implementirane sve osnovne operacije s kompleksnim brojevima.
 * 
 * @author Valentina Križ
 *
 */
public class ComplexNumber {
	private double real;
	private double imaginary;
	
	/**
	 * Konstruktor koji stvara kompleksni broj sa zadanim realnim i imaginarnim dijelom.
	 * 
	 * @param real realni dio
	 * @param imaginary imaginarni dio
	 */
	public ComplexNumber(double real, double imaginary) {
		this.real = real;
		this.imaginary = imaginary;
	}
	
	/**
	 * Metoda koja vraća kompleksan broj sa zadanim realnim dijelom.
	 * 
	 * @param real realni dio
	 * @return novonastali kompleksni broj
	 */
	public static ComplexNumber fromReal(double real) {
		return new ComplexNumber(real, 0);
	}
	
	/**
	 * Metoda koja vraća kompleksan broj sa zadanim imaginarnim dijelom.
	 * 
	 * @param imaginary imaginarni dio
	 * @return novonastali kompleksni broj
	 */
	public static ComplexNumber fromImaginary(double imaginary) {
		return new ComplexNumber(0, imaginary);
	}
	
	
	/**
	 * Metoda koji vraća kompleksan broj dobiven pomoću zadanog kuta iz trigonometrijskog oblika.
	 * rcos(θ)+rsin(θ)⋅i
	 * 
	 * @param magnitude
	 * @param angle
	 * @return
	 */
	public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
		return new ComplexNumber(
					magnitude * Math.cos(angle),
					magnitude * Math.sin(angle));
	}

	/**
	 * Pomoćna metoda koja služi za dohvaćanje imaginarnog dijela broja iz stringa.
	 * 
	 * @param s string koji sadrži imaginarnio dio broja
	 * @return double vrijednost broja
	 */
	private static double parseImaginary(String s) {
		// u slučaju da je imaginarni dio samo "-i" ili "i" vrijednost je -1, odnosno 1
		double imaginary = (s.startsWith("-")) ? -1 : 1;
		
		if(!s.equals("+i") && !s.equals("-i")) {
			imaginary = Double.parseDouble(s.substring(0, s.length() - 1));
		}
		return imaginary;
	}
	
	/**
	 * Metoda koja vraća kompleksni broj generiran iz zadanog stringa, ako string
	 * predstavlja zadovoljavajući oblik kompleksnog broja.
	 * Dozvoljeni oblici su: a+bi, a-bi, -a+bi, -a-bi, a, -a, bi, -bi,
	 * gdje su a i b realni brojevi.
	 * Dozvoljeni su i realni brojevi oblika .3 (=0.3).
	 * Također, u stringu se smiju nalaziti razmaci na proizvodnim mjestima.
	 * 
	 * @param s string koji sadrži kompleksni broj
	 * @return novonastali kompleksni broj
	 * @throws NumberFormatException ako string nije dozvoljenog oblika
	 */
	public static ComplexNumber parse(String s) {
		// izbacimo sve razmake iz stringa
		// originalni string s ostavljamo za ispis ako string nije dobrog formata
		String newS = s.replaceAll("\\s+","");
			
		// ako string zapocinje s +- ili -+ odmah bacamo iznimku
		if(newS.startsWith("+-") || newS.startsWith("-+")) {
			throw new NumberFormatException(s + " nije kompleksan broj.");
		}
		
		// makni nepotrebni + ako string počinje sa +
		if(newS.startsWith("+")) {
			newS = newS.substring(1);
		}
		
		double imaginary = 0.0;
		double real = 0.0;
		int dividerIndex = Math.max(newS.lastIndexOf("+"), newS.lastIndexOf("-"));
		
		// ako se broj sastoji od samo realnog ili samo imaginarnog dijela
		if(dividerIndex == -1 || dividerIndex == 0) {
			if(newS.contains("i")) {
				// samo imaginarni dio (ili neispravan format)
				try {
					imaginary = parseImaginary(newS);
				} catch (NumberFormatException ex) {
					throw new NumberFormatException(s + " nije kompleksan broj.");
				}
			} else {
				// samo realni dio (ili neispravan format)
				try {
					real = Double.parseDouble(newS);
				} catch (NumberFormatException ex) {
					throw new NumberFormatException(s + " nije kompleksan broj.");
				}
				
			}
		} else {
			// ako broj ima i realni i imaginarni dio (ili neispravan format)
			try {
				real = Double.parseDouble(newS.substring(0, dividerIndex));
				imaginary = parseImaginary(newS.substring(dividerIndex));
			} catch (NumberFormatException ex) {
				throw new NumberFormatException(s + " nije kompleksan broj.");
			}
		}
		
		return new ComplexNumber(real, imaginary);
	}
	
	/**
	 * Metoda koja vraća realni dio kompleksnog broja.
	 * 
	 * @return realni dio
	 */
	public double getReal() {
		return real;
	}
	
	/**
	 * Metoda koja vraća imaginarni dio broja.
	 * 
	 * @return imaginarni dio
	 */
	public double getImaginary() {
		return imaginary;
	}
	
	/**
	 * Metoda koja računa modul kompleksnog broja.
	 * 
	 * @return modul broja
	 */
	public double getMagnitude() {
		return Math.sqrt(Math.pow(getReal(), 2) + Math.pow(getImaginary(), 2));
	}
	
	/**
	 * Metoda koja računa argument kompleksnog broja, ako je za njega
	 * definiran argument (broj je različit od 0).
	 * 
	 * @return kut
	 * @throws IllegalArgumentException ako je kompleksni broj 0
	 */
	public double getAngle() {
		double real = getReal();
		double imaginary = getImaginary();
		
		// ako je broj 0, kut nije definiran
		if(real == 0 && imaginary == 0) {
			throw new IllegalArgumentException("Za imaginarni broj 0 kut nije definiran.");
		}
		
		double angle = Math.atan(imaginary / real);
		
		// ovisno o kvadrantu u kojem se tocka nalazi potrebno je prilagoditi vrijednost kuta.
		if(real >= 0) {
			if(imaginary >= 0) {
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
	
	/**
	 * Metoda koja zbraja dva kompleksna broja.
	 * 
	 * @param c drugi kompleksni broj
	 * @return rezultat zbrajanja
	 */
	public ComplexNumber add(ComplexNumber c) {
		return new ComplexNumber(
					real + c.getReal(),
					imaginary + c.getImaginary());
	}
	
	/**
	 * Metoda koja oduzima zadani kompleksni broj od početnog broja.
	 * 
	 * @param c drugi kompleksni broj (umanjitelj)
	 * @return rezultat oduzimanja
	 */
	public ComplexNumber sub(ComplexNumber c) {
		return new ComplexNumber(
					real - c.getReal(),
					imaginary - c.getImaginary());
	}
	
	/**
	 * Metoda koja množi dva kompleksna broja.
	 * (ac−bd)+(ad+bc)i
	 * 
	 * @param c drugi kompleksni broj
	 * @return rezultat množenja
	 */
	public ComplexNumber mul(ComplexNumber c) {
		return new ComplexNumber(
					real * c.getReal() - imaginary * c.getImaginary(), 
					real * c.getImaginary() + imaginary * c.getReal());
	}
	
	/**
	 * Metoda koji dijeli kompleksni broj sa drugim kompleksnim brojem 
	 * ((ac + bd) + (bc − ad)i) / (c^2 + d^2)
	 * 
	 * @param c djelitelj
	 * @return rezultat dijeljenja
	 */
	public ComplexNumber div(ComplexNumber c) {
		double cReal = c.getReal();
		double cImaginary = c.getImaginary();
		
		// nema dijeljenja s 0
		if(cReal == 0 && cImaginary == 0) {
			throw new IllegalArgumentException("Dijeljenje s nulom nije dopušteno.");
		}
		
		double denominator = cReal * cReal + cImaginary * cImaginary;
		
		return new ComplexNumber(
					(real * cReal + imaginary * cImaginary) / denominator,
					(imaginary * cReal - real * cImaginary) / denominator);
	}
	
	/**
	 * Metoda koja računa n-tu potenciju kompleksnog broja.
	 * [r(cos θ + i * sin θ)]^n = r^n * (cos nθ + i * sin nθ)
	 * 
	 * @param n potencija koju korisnik želi izračunati
	 * @return rezultat potenciranja
	 */
	public ComplexNumber power(int n) {
		return ComplexNumber.fromMagnitudeAndAngle(
								Math.pow(getMagnitude(), n),
								n * getAngle());
	}
	
	/**
	 * Metoda koja računa n-te korijene kompleksnog broja.
	 * 
	 * @param n korijen koji korisnik želi izračunati
	 * @return rezultat
	 */
	public ComplexNumber[] root(int n) {
		ComplexNumber[] roots = new ComplexNumber[n]; 

		for(int k = 0; k < n; ++k) {
			roots[k] = ComplexNumber.fromMagnitudeAndAngle(
										Math.pow(getMagnitude(), 1.0 / n),
										(getAngle() + 2 * k * Math.PI) / n);
		}
		
		return roots;
	}
	
	/**
	 * Metoda koja generira string za prikaz kompleksnog broja.
	 *
	 * @return generirani string
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		if(real != 0) {
			str.append(real);
		}
		
		if(imaginary != 0) {
			if(real != 0 && imaginary > 0) {
				str.append("+");
			} 
			str.append(imaginary);
			str.append("i");
		}
		
		return str.toString();
	}
}
