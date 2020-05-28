package hr.fer.zemris.java.gui.charts;

/**
 * Razred predstavlja objekt koji sadrži
 * read-only vrijednosti x i y.
 * 
 * @author Valentina Križ
 *
 */
public class XYValue {
	/**
	 * x vrijednost.
	 */
	private int x;
	
	/**
	 * y vrijednost.
	 */
	private int y;
	
	/**
	 * Konstruktor koji prima vrijednosti
	 * x i y.
	 * 
	 * @param x
	 * @param y
	 */
	public XYValue(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Getter za varijablu x.
	 * 
	 * @return vrijednost varijable x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Getter za varijablu y.
	 * 
	 * @return vrijednost varijable y
	 */
	public int getY() {
		return y;
	}
}
