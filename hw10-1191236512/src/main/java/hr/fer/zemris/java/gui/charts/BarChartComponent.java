package hr.fer.zemris.java.gui.charts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * Razred predstavlja komponentu koja na svojoj površini
 * stvara prikaz podataka koji su definirani primljenim
 * objektom, tj. crta stupčasti dijagram.
 * 
 * Nasljeđuje razred JComponent.
 * 
 * @author Valentina Križ
 *
 */
public class BarChartComponent extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Razmak koji se dodaje radi preglednosti.
	 */
	private static final int PADDING = 10;
	
	/**
	 * Referenca na BarChart koji sadrži informacije
	 * za crtanje.
	 */
	private BarChart chart;
	
	/**
	 * Trenutna pozicija na x-osi za crtanje.
	 */
	private int x;
	
	/**
	 * Trenutna pozicija na y-osi za crtanje.
	 */
	private int y;
	
	/**
	 * Referenca na Graphics2D objekt po kojem crtamo.
	 */
	private Graphics2D g2d;
	
	/**
	 * Gornji rub prostora po kojem možemo crtati.
	 */
	int top;
	
	/**
	 * Donji rub prostora po kojem možemo crtati.
	 */
	int bottom;
	
	/**
	 * Lijevi rub prostora po kojem možemo crtati.
	 */
	int left;
	
	/**
	 * Desni rub prostora po kojem možemo crtati.
	 */
	int right;
	
	/**
	 * Konstruktor koji prima referencu
	 * na BarChart objekt.
	 * 
	 * @param chart
	 */
	public BarChartComponent(BarChart chart) {
		super();
		this.chart = chart;
	}
	
	/**
	 * {@inheritDoc}}
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g2d = (Graphics2D) g;
		
		Dimension size = getSize();
		Insets insets = getInsets();
		
		top = insets.top + PADDING;
		bottom = size.height - insets.top - insets.bottom;
		left = insets.left;
		right = size.width - insets.left - insets.right - PADDING;
		
		FontMetrics font = g2d.getFontMetrics();
		
		drawAxesTitles(font);
		drawXs(font);
		int yGap = drawYs(font);
		drawAxes();
		drawBars(yGap);
	}

	/**
	 * Metoda ispisuje opis x i y osi i priprema
	 * pozicije x i y za iscrtavanje vrijednosti po osima.
	 * 
	 * @param font
	 */
	private void drawAxesTitles(FontMetrics font) {
		// ispis za x os
		g2d.drawString(chart.xTitle, (left + right - font.stringWidth(chart.xTitle)) / 2, bottom - font.getDescent());
	
		// rotiranje i ispis za y-os
		AffineTransform defaultAt = g2d.getTransform();
		defaultAt.rotate(-Math.PI / 2);
		g2d.setTransform(defaultAt);
		g2d.drawString(chart.yTitle, ( -(bottom + top + font.stringWidth(chart.yTitle)) / 2), left + font.getAscent());
		
		// rotiranje unazad
		defaultAt.rotate(Math.PI / 2);
		g2d.setTransform(defaultAt);
		
		// priprema pozicije za iscrtavanje vrijednosti po osima
		x = left + font.getHeight() + PADDING;
		y = bottom - font.getHeight() - PADDING;
	}
	
	/**
	 * Metoda iscrtava vrijedosti po x-osi i priprema
	 * poziciju y za crtanje x-osi.
	 * 
	 * @param font
	 */
	private void drawXs(FontMetrics font) {
		List<String> xValues = new ArrayList<>();
		
		for(XYValue value : chart.values) {
			xValues.add(Integer.toString(value.getX()));
		}
		
		int columnWidth = (right - x) / (xValues.size());
		
		for(int i = 0, size = xValues.size(); i < size; ++i) {
			g2d.drawString(xValues.get(i), 
					x + columnWidth / 2 + i * columnWidth,
					y);
		}
		
		// priprema pozicije za crtanje x-osi
		y -= font.getHeight() + PADDING;
	}

	/**
	 * Metoda iscrtava vrijednosti po y-osi i priprema
	 * poziciju x za crtanje y-osi.
	 * 
	 * @param font
	 * @return razmak u pikselima između 2 vrijednosti
	 */
	private int drawYs(FontMetrics font) {
		List<String> yValuesToDisplay = new ArrayList<>();
		
		int cnt = 0;
		int maxStringWidth = 0;
		
		while(chart.minY + cnt * chart.yGap < chart.maxY + chart.yGap) {
			yValuesToDisplay.add(Integer.toString(chart.minY + cnt * chart.yGap));
			maxStringWidth = Math.max(font.stringWidth(Integer.toString(cnt * chart.yGap)), maxStringWidth);
			++cnt;
		}
		
		int realGap = (y - top) / (yValuesToDisplay.size() - 1);
		
		for(int i = 0, size = yValuesToDisplay.size(); i < size; ++i) {
			g2d.drawString(yValuesToDisplay.get(i), 
					x + maxStringWidth - font.stringWidth(yValuesToDisplay.get(i)), 
					y + font.getHeight() / 2 - i * realGap);
		}
		
		// priprema pozicije za crtanje y-osi
		x += maxStringWidth + PADDING;
		return realGap;
	}
	
	/**
	 * Metoda crta x i y os.
	 */
	private void drawAxes() {
		// x-os
		g2d.drawLine(x, y, right, y);	
		// y-s
		g2d.drawLine(x, y, x, top);
		
		drawArrowHeads();
	}
	
	/**
	 * Metoda crta vrhove strelica na x i y osi.
	 */
	private void drawArrowHeads() {
		Polygon arrowHead = new Polygon();  
	    arrowHead.addPoint(right + PADDING, y);
	    arrowHead.addPoint(right, y - 5);
	    arrowHead.addPoint(right, y + 5);
	    g2d.fill(arrowHead);
	   
	    arrowHead = new Polygon();
	    arrowHead.addPoint(x, top - PADDING);
	    arrowHead.addPoint(x - 5, top);
	    arrowHead.addPoint(x + 5, top);
	    g2d.fill(arrowHead);
	}
	
	/**
	 * Metoda crta pravokutnike koji predstavljaju 
	 * stupčasti dijagram.
	 * 
	 * @param yGap udaljenost u pikselima između
	 * 			vrijednosti na y-osi
	 */
	private void drawBars(int yGap) {
		int size = chart.values.size();
		int columnWidth = (right - x) / size;
		int yUnitLength = yGap / chart.yGap;

		
		for(int i = 0; i < size; ++i) {
			XYValue val = chart.values.get(i);

			g2d.setColor(Color.ORANGE);
			
			g2d.fillRect(x + i * columnWidth,
					y - yUnitLength * val.getY(),
					columnWidth,
					yUnitLength * val.getY());
			
			g2d.setColor(Color.BLACK);
			
			g2d.drawRect(x + i * columnWidth,
					y - yUnitLength * val.getY(),
					columnWidth,
					yUnitLength * val.getY());
		}
	}
}
