package hr.fer.zemris.math;

/**
 * Razred modelira polinom nad kompleksnim brojevima.
 * f(z) oblika z0*(z-z1)*(z-z2)*...*(z-zn), gdje su z1 do zn njegove
 * nultočke, a z0 konstanta 
 * 
 * @author Valentina Križ
 *
 */
public class ComplexRootedPolynomial {
	Complex constant;
	Complex[] roots;
	
	/**
	 * Konstruktor koji postavlja nultočke i konstantu
	 * na zadane vrijednosti.
	 * 
	 * @param constant
	 * @param roots
	 */
	public ComplexRootedPolynomial(Complex constant, Complex ...roots) {
		this.constant = constant;
		this.roots = roots;
	}
	
	/**
	 * Metoda za izračun vrijednosti polinoma u danoj
	 * kompleksnoj točki.
	 * 
	 * @param z
	 * @return vrijednost polinoma u z
	 */
	public Complex apply(Complex z) {
		Complex res = new Complex(1, 0);
		res = res.multiply(constant);
		
		for(Complex root : roots) {
			res = res.multiply(z.sub(root));
		}
		
		return res;
	}
	
	/**
	 * Metoda za pretvorbu polinoma u oblik
	 * zn*z^n+zn-1*z^(n-1)+...+z2*z^2+z1*z+z0, gdje su z0 do zn 
	 * koeficijenti koji pišu uz odgovarajuće potencije od z 
	 * 
	 * @return polinom u zadanom obliku
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial res = new ComplexPolynomial(constant);
		
		for(Complex root : roots) {
			res = res.multiply(new ComplexPolynomial(root.negate(), Complex.ONE));
		}
		
		return res;
	}
	
	/**
	 * Metoda za zapis kompleksnog broja u stringu.
	 * Zapis oblika f(z) = (2.0+i0.0)*(z-(1.0+i0.0))*(z-(-1.0+i0.0))*(z-(0.0+i1.0))*(z-(0.0-i1.0))
	 * 
	 * @return string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("f(z) = (");
		sb.append(constant);
		sb.append(")");
		
		for(Complex root : roots) {
			sb.append("*(z-(");
			sb.append(root);
			sb.append("))");
		}
		
		return sb.toString();
	}
	
	/**
	 * Metoda za pronalazak nultočke koja je najbliže zadanom
	 * broju, a nalazi se unutar dopuštene udaljenosti.
	 * Ako takva nultočka ne postoji, metoda vraća -1.
	 * Indeksi nultočaka se broje od 0.
	 * 
	 * @param z
	 * @param treshold
	 * @return indeks nultočke ili -1 ako ona ne postoji
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int index = -1;
		double minDistance = Double.MAX_VALUE;
		
		for(int i = 0, len = roots.length; i < len; ++i) {
			double distance = z.sub(roots[i]).module();
			if(distance < treshold && distance < minDistance) {
				minDistance = distance;
				index = i;
			}
		}

		return index;
	}
}
