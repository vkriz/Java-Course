package hr.fer.zemris.math;

/**
 * Razred modelira 2D vektor čije su komponente realni
 * brojevi x i y. Razred nudi operacije translatiranja,
 * rotacije i skaliranja vektora.
 * 
 * @author Valentina Križ
 *
 */
public class Vector2D {
	private double x;
	private double y;
	
	/**
	 * Konstruktor koji x i y postavlja na 
	 * zadane vrijednosti. 
	 * 
	 * @param x
	 * @param y
	 */
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
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
	 * Metoda koja vektor translatira za zadani offset.
	 * 
	 * @param offset
	 */
	public void translate(Vector2D offset) {
		x += offset.getX();
		y += offset.getY();
	}
	
	/**
	 * Metoda koja stvara novi vektor koji je rezultat
	 * translatacije vektora za zadani offset.
	 * 
	 * @param offset
	 * @return rezultat vektor
	 */
	public Vector2D translated(Vector2D offset) {
		Vector2D newVec = copy();
		newVec.translate(offset);
		return newVec;
	}
	
	/**
	 * Metoda koja rotira vektor za zadani kut.
	 * 
	 * @param angle kut
	 */
	public void rotate(double angle) {
		double oldX = x;
		x = x * Math.cos(angle) - y * Math.sin(angle);
		y = oldX * Math.sin(angle) + y * Math.cos(angle);
	}
	
	/**
	 * Metoda koja stvara novi vektor koji je rezultat
	 * rotacije vektora za zadani kut.
	 * 
	 * @param angle kut
	 * @return rezultat vektor
	 */
	public Vector2D rotated(double angle) {
		Vector2D newVec = copy();
		newVec.rotate(angle);
		return newVec;
	}
	
	/**
	 * Metoda koja skalira vektor zadanom vrijednosti.
	 * 
	 * @param scaler vrijednost skaliranja
	 */
	public void scale(double scaler) {
		x *= scaler;
		y *= scaler;
	}
	
	/**
	 * Metoda koja stvara novi vektor koji je rezultat 
	 * skaliranja.
	 * 
	 * @param scaler vrijednost skaliranja
	 * @return rezultat vektor
	 */
	public Vector2D scaled(double scaler) {
		Vector2D newVec = copy();
		newVec.scale(scaler);
		return newVec;
	}
	
	/**
	 * Metoda koja kopira vektor.
	 * 
	 * @return kopija vektora
	 */
	public Vector2D copy() {
		return new Vector2D(x, y);
	}
}
