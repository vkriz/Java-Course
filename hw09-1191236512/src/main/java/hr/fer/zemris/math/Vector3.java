package hr.fer.zemris.math;

/**
 * Razred modelira 3D vektor čije su komponente realni
 * brojevi x, y i z. Razred nudi operacije klasične operacije
 * nad 3D vektorima.
 * 
 * @author Valentina Križ
 *
 */
public class Vector3 {
	double x;
	double y;
	double z;
	
	/**
	 * Konstruktor koji prima vrijednosti
	 * x, y i z.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Metoda za računanje norme vektora.
	 * 
	 * @return norma vektora
	 */
	public double norm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
	
	/**
	 * Metoda za normalizaciju vektora.
	 * Vraća novi vektor, originalni vektor
	 * ostaje nepromijenjen.
	 * 
	 * @return normalizirani vektor
	 */
	public Vector3 normalized() {
		double vecNorm = norm();
		
		if(Math.abs(vecNorm) > 10e-5) {
			return scale(1 / vecNorm);
		}
		return this;
	}
	
	/**
	 * Metoda za zbrajanje dvaju vektora.
	 * Vraća novi vektor, oba originalna vektora
	 * ostaju nepromijenjena.
	 * 
	 * @param other drugi vektor
	 * @return zbroj dva vektora
	 */
	public Vector3 add(Vector3 other) {
		return new Vector3(x + other.x, y + other.y, z + other.z);
	}
	
	/**
	 * Metoda za oduzimanje dvaju vektora.
	 * Vraća novi vektor, oba originalna vektora
	 * ostaju nepromijenjena.
	 * 
	 * @param other drugi vektor
	 * @return razlika dva vektora
	 */
	public Vector3 sub(Vector3 other) {
		return new Vector3(x - other.x, y - other.y, z - other.z);
	}
	
	/**
	 * Metoda za računanje skalarnog produkta
	 * dvaju vektora.
	 * 
	 * @param other drugi vektor
	 * @return skalarni produkt
	 */
	public double dot(Vector3 other) {
		return x*other.x + y*other.y + z*other.z;
	}
	
	/**
	 * Metoda za računanje vektorskog produkta
	 * dvaju vektora. 
	 * Vraća novi vektora, oba originalna vektora
	 * ostaju nepromijenjena.
	 * 
	 * @param other drugi vektor
	 * @return vektorski produkt
	 */
	public Vector3 cross(Vector3 other) {
		return new Vector3(y*other.z - z*other.y,
					z*other.x - x*other.z,
					x*other.y - y*other.x);
	}
	
	/**
	 * Metoda za skaliranje vektora s danim faktorom.
	 * Vraća novi vektor, originalni vektor 
	 * ostaje nepromijenjen.
	 * 
	 * @param s faktor skaliranja
	 * @return skalirani vektor
	 */
	public Vector3 scale(double s) {
		return new Vector3(s * x, s * y, s * z);
	}
	
	/**
	 * Metoda za računanje kosinusa kuta
	 * između dvaju vektora.
	 * 
	 * @param other drugi vektor
	 * @return kosinus kuta
	 */
	public double cosAngle(Vector3 other) {
		if(norm() != 0 && other.norm() != 0) {
			return this.dot(other) / (norm() * other.norm());
		}
		return 0;
	}
	
	/**
	 * Getter za varijablu x.
	 * 
	 * @return vrijednost varijable x
	 */
	public double getX() {
		return x;
	}
	
	/**
	 * Getter za varijablu y.
	 * 
	 * @return vrijednost varijable y
	 */
	public double getY() {
		return y;
	}
	
	/**
	 * Getter za varijablu z.
	 * 
	 * @return vrijednost varijable z
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Metoda za pretvorbu vektora u polje
	 * realnih brojeva.
	 * 
	 * @return polje realnih brojeva s vrijednostima
	 * 			x, y i z
	 */
	public double[] toArray() {
		return new double[] {x, y, z};
	}
	
	/**
	 * Metoda za prikaz vektora u obliku stringa.
	 * 
	 * @return string oblika (x, y, z)
	 */
	@Override
	public String toString() {
		return String.format("(%f, %f, %f)", x, y, z);
	}
}
