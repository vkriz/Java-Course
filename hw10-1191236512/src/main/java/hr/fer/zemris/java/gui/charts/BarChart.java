package hr.fer.zemris.java.gui.charts;

import java.util.List;

/**
 * Razred prestavlja objekt koji pamti
 * podatke koji se trebaju prikazati na 
 * stupčastom dijagramu.
 * 
 * @author Valentina Križ
 *
 */
public class BarChart {
	/**
	 * Lista vrijednosti za prikaz.
	 */
	List<XYValue> values;
	
	/**
	 * Opis x-osi.
	 */
	String xTitle;
	
	/**
	 * Opis y-osi.
	 */
	String yTitle;
	
	/**
	 * Minimalni y koji se prikazuje na y-osi.
	 */
	int minY;
	
	/**
	 * Minimalni y koji se prikazuje na y-osi.
	 */
	int maxY;
	
	/**
	 * Razmak između dva susjedna y-a koji
	 * se prikazuju na y-osi.
	 */
	int yGap;
	
	/**
	 * Konstruktor koji prima listu vrijednosti za prikaz,
	 * opis x-osi, opis y-osi, minimalni i maksimalni y koji se 
	 * prikazuje na y-osi i razmak između dva susjedna y-a koji
	 * se prikazuju na y-osi.
	 * 
	 * @param values
	 * @param xTitle
	 * @param yTitle
	 * @param minY
	 * @param maxY
	 * @param yGap
	 * 
	 * @throws IllegalArgumentException ako je minimalni y negativan,
	 * 				ako je neki od y-a u zadanim vrijednostima manji od minimalnog
	 * 				ili ako maksimalni y nije strogo veći od minimalnog
	 */
	public BarChart(List<XYValue> values, String xTitle, String yTitle, int minY, int maxY, int yGap) {
		this.values = values;
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.yGap = yGap;
		
		if(minY < 0) {
			throw new IllegalArgumentException("Minimalni y ne može biti negativan.");
		}
		this.minY = minY;
		
		for(XYValue value : values) {
			if(value.getY() < minY) {
				throw new IllegalArgumentException("Svi y-i moraju biti strogo veći od minimalnog.");
			}
		}
		
		if(maxY <= minY) {
			throw new IllegalArgumentException("Maksimalni y mora biti strogo veći od minimalnog y-a.");
		}
		this.maxY = maxY;
	}
}
