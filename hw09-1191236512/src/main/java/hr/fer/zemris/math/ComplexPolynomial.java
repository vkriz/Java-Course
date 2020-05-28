package hr.fer.zemris.math;

/**
 * Razred modelira polinom nad kompleksnim brojevima.
 * f(z) oblika zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0, gdje su z0 do zn 
 * koeficijenti koji pišu uz odgovarajuće potencije od z 
 * 
 * @author Valentina Križ
 *
 */
public class ComplexPolynomial {
	Complex[] factors;
	
	/**
	 * Konstruktor koji postavlja koeficijente na predane vrijednosti.
	 * Redoslijed faktora predanih u konstruktoru s lijeva na desno se tumači kao z0, z1, z2, ...
	 * 
	 * @param factors
	 */
	public ComplexPolynomial(Complex ...factors) {
		this.factors = factors;
	}
	
	/**
	 * Metoda koja vraća stupanj polinoma.
	 * 
	 * @return stupanj polinoma
	 */
	public short order() {
		return (short) (factors.length - 1);
	}
	
	/**
	 * Metoda za množenje dvaju polinoma.
	 * Metoda stvara i vraća novi polinom, originalni
	 * polinomi ostaju nepromijenjeni. 
	 * 
	 * @param p polinom s kojim množimo
	 * @return umnožak polinoma
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Complex[] newFactors = new Complex[p.order() + order() + 1];
		
		// koef. uz z^n jednak je sumi umnoška svih parova z^i * z^n-i
		for(int n = 0, len = newFactors.length; n < len; ++n) {
			Complex factor = new Complex();
			
			for(int i = 0; i <= order(); ++i) {
				if(n - i > p.order() || n - i < 0) {
					continue;
				}
				factor = factor.add(factors[i].multiply(p.factors[n - i]));
			}
			newFactors[n] = factor;
		}
		
		return new ComplexPolynomial(newFactors);
	}
	
	/**
	 * Metoda za deriviranje polinoma.
	 * Vraća novi polinom, originalni polinom ostaje
	 * nepromijenjen.
	 * 
	 * @return derivirani polinom
	 */
	public ComplexPolynomial derive() {
		Complex[] derivedFactors = new Complex[factors.length - 1];
		
		for(int i = 0, len = factors.length; i < len - 1; ++i) {
			derivedFactors[i] = factors[i + 1].multiply(new Complex(i + 1, 0));
		}
		
		return new ComplexPolynomial(derivedFactors);
	}
	
	/**
	 * Metoda za izračun vrijednosti polinoma u danoj
	 * kompleksnoj točki.
	 * 
	 * @param z
	 * @return vrijednost polinoma u z
	 */
	public Complex apply(Complex z) {
		Complex res = new Complex(0, 0);
		
		for(int i = 0, len = factors.length; i < len; ++i) {
			res = res.add(factors[i].multiply(z.power(i)));
		}
		
		return res;
	}
	
	/**
	 * Metoda za zapis kompleksnog broja u stringu.
	 * Zapis oblika f(z) = (2.0+i0.0)*z^4+(0.0+i0.0)*z^3+(0.0+i0.0)*z^2+(0.0+i0.0)*z^1+(-2.0+i0.0)
	 * 
	 * @return string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("f(z) = ");
		
		for(int len = factors.length, i = len - 1; i > 0; --i) {
			sb.append("(");
			sb.append(factors[i]);
			sb.append(String.format(")*z^%d+", i));
		}
		
		sb.append("(" + factors[0] + ")");
		
		return sb.toString();
	}
}
