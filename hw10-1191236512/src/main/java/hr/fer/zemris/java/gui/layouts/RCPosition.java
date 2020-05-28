package hr.fer.zemris.java.gui.layouts;

/**
 * Razred predstavlja poziciju u mreži, u 
 * obliku (redak, stupac).
 * 
 * @author Valentina Križ
 *
 */
public class RCPosition {
	private int row;
	private int column;
	
	/**
	 * Konstruktor koji prima redak i stupac.
	 * 
	 * @param row
	 * @param column
	 */
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	/**
	 * Getter za varijablu row.
	 * 
	 * @return vrijednost varijable row
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter za varijablu column.
	 * 
	 * @return vrijednost varijable column
	 */
	public int getColumn() {
		return column;
	}
}
