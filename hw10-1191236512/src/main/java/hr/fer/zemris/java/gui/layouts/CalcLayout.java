package hr.fer.zemris.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * Razred predstavlja jedan upravljač razmještaja (layout manager)
 * koji implementira sučelje java.awt.LayoutManager2.
 * Komponente su raspoređene u mrežu 5 redaka i 7 stupaca.
 * 
 * @author Valentina Križ
 *
 */
public class CalcLayout implements LayoutManager2 {
	/**
	 * Broj redaka u layoutu.
	 */
	private final int NUM_ROWS = 5;
	
	/**
	 * Broj stupaca u layoutu.
	 */
	private final int NUM_COLUMNS = 7;
	
	/**
	 * Razmak između redaka i stupaca.
	 */
	private int gap;
	
	/**
	 * Komponente koje layout sadrži.
	 */
	private Component[][] components;
	
	/**
	 * Konstruktor koji prima željeni razmak između redaka i 
	 * stupaca u pikselima. 
	 * 
	 * @param space razmak
	 */
	public CalcLayout(int gap) {
		if(gap < 0) {
			throw new CalcLayoutException("Razmak ne smije biti negativan.");
		}
		
		components = new Component[NUM_ROWS][NUM_COLUMNS];
		
		this.gap = gap;
	}
	
	/**
	 * Default konstruktor, postavlja razmak
	 * između redaka i stupaca od 0 piksela.
	 */
	public CalcLayout() {
		this(0);
	}
	
	private void validatePosition(RCPosition pos) {
		int row = pos.getRow();
		int column = pos.getColumn();
		
		if(row < 1|| column < 1 || row > NUM_ROWS || column > NUM_COLUMNS || (row == 1 && (column > 1 && column < 6))) {
			throw new CalcLayoutException("Nelegalna pozicija " + row + " " + column + ".");
		}
	}
	
	/**
	 * Metoda tvornica koja prima tekstovnu specifikaciju
	 * i vraća odgovarajući objekt tipa RCPosition.
	 * Specifikacija je oblika "row, column".
	 * 
	 * @param text specifikacija
	 * @return odgovarajući RCPosition objekt
	 * @throws IllegalArgumentException ako je tekst
	 * 			neispravnog formatata, tj. ne može se parsirati
	 */
	public static RCPosition parse(String text) {
		String[] parts = text.trim().split(",");
		if(parts.length != 2) {
			System.out.println("Neispravan format.");
		}
		
		try {
			int row = Integer.parseInt(parts[0]);
			int column = Integer.parseInt(parts[1]);
			
			return new RCPosition(row, column);
			
		} catch(NumberFormatException ex) {
			throw new IllegalArgumentException("Neispravan format.");
		}
	}

	/**
	 * Metoda za dodavanje komponente sa zadanim imenom.
	 * Ovdje baca UnsupportedOperationException.
	 * 
	 * @param name
	 * @param comp
	 * @throws UnsupportedOperationException
	 */
	@Override
	public void addLayoutComponent(String name, Component comp) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Metoda uklanja zadanu komponentu iz layouta.
	 * 
	 * @param comp
	 */
	@Override
	public void removeLayoutComponent(Component comp) {
		// ako je predan null ne moramo ni tražiti
		if(comp == null) {
			return;
		}
		
		// inače pronađi komponentu i postavi na null
		for(int row = 0; row < NUM_ROWS; ++row) {
			for(int col = 0; col < NUM_COLUMNS; ++col) {
				if(components[row][col] != null && components[row][col].equals(comp)) {
					components[row][col] = null;
					return;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return calcDimension(parent, Component::getPreferredSize);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return calcDimension(parent, Component::getMinimumSize);
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public Dimension maximumLayoutSize(Container target) {
		return calcDimension(target, Component::getMaximumSize);
	}
	
	/**
	 * Pomoćno sučelje za izračun dimenzije layouta.
	 * 
	 * @author Valentina Križ
	 *
	 */
	private interface SizeGetter {
		/**
		 * Metoda dohvaća dimenziju predane komponente.
		 * 
		 * @param comp
		 * @return dimenzija komponente
		 */
		Dimension getSize(Component comp);
	} 
	
	/**
	 * Metoda računa dimenziju layouta ovisno o dimenzijama
	 * komponenti koji sadrži.
	 * 
	 * @param target
	 * @param getter 
	 * @return dimenzije layouta
	 */
	private Dimension calcDimension(Container target, SizeGetter getter) {
		int maxWidth = 0;
		int maxHeight = 0;
		
		for(int row = 0; row < NUM_ROWS; ++row) {
			for(int col = 0; col < NUM_COLUMNS; ++col) {
				Component c = components[row][col];
				if(c != null) {
					Dimension cdim = getter.getSize(c);
					
					// 5 pločica spojenih u jednu
					if(row == 0 && col == 0) {
						maxWidth = Math.max(maxWidth, (cdim.width - 4 * gap) / 5);
						maxHeight = Math.max(maxHeight, cdim.height);
					} else {
						maxWidth = Math.max(maxWidth, cdim.width);
						maxHeight = Math.max(maxHeight, cdim.height);
					}
				}
			}
		}
		
		Insets insets = target.getInsets();
		return new Dimension( maxWidth * NUM_COLUMNS + gap * (NUM_COLUMNS - 1) + insets.left + insets.right,
				maxHeight * NUM_ROWS + gap * (NUM_ROWS - 1) + insets.top + insets.bottom);
	}

	/**
	 * Metoda za izradu razmještaja.
	 * 
	 * @param parent container u koji se slažu komponente
	 */
	@Override
	public void layoutContainer(Container parent) {
		if(parent == null) {
			return;
		}
		
		Dimension parentSize = parent.getSize();
		Insets insets = parent.getInsets();
		
		double width = (parentSize.width - insets.left - insets.right - (NUM_COLUMNS - 1) * gap) / NUM_COLUMNS;
		double height = (parentSize.height - insets.top - insets.bottom - (NUM_ROWS - 1) * gap) / NUM_ROWS;
		
		int x = insets.left;
		int y = insets.top;
		
		int right = (int)Math.floor(width);
		int down = (int)Math.floor(height);
		
		int extraWidthPixels = parentSize.width - insets.left - insets.right - right * NUM_COLUMNS - gap * (NUM_COLUMNS - 1);
		int extraHeightPixels = parentSize.height - insets.top - insets.bottom - down * NUM_ROWS - gap * (NUM_ROWS - 1);
		
		for(int row = 0; row < NUM_ROWS; ++row) {
			int addPixelHeight = addExtraHeightPixel(extraHeightPixels, row);
			
			x = insets.left;
			if(row != 0) {
				y += gap;
			}
			for(int col = 0; col < NUM_COLUMNS; ++col) {
				int addPixelWidth = addExtraWidthPixel(extraWidthPixels, col);
				
				if(row == 0) {
					if(col > 0 && col < 5) {
						continue;
					} else if(col == 0){
						// ako je "velika" komponenta
						int computedWidth = right * 5 + gap * 4 + addExtraWidthPixel(extraWidthPixels, 0) 
																+ addExtraWidthPixel(extraWidthPixels, 1) 
																+ addExtraWidthPixel(extraWidthPixels, 2) 
																+ addExtraWidthPixel(extraWidthPixels, 3) 
																+ addExtraWidthPixel(extraWidthPixels, 4);
						if(components[row][col] != null) {
							components[row][col].setBounds(x,
									y,
									computedWidth,
									down + addPixelHeight);	
						}
						
						x += computedWidth;
						continue;
					}
				}
				
				if(col != 0) {
					x += gap;
				}

				if(components[row][col] != null) {
					components[row][col].setBounds(x, y, right + addPixelWidth, down + addPixelHeight);	
				}
				
				x += right + addPixelWidth;
			}
			y += down + addPixelHeight;
		}
		
		
	}

	/**
	 * Metoda za dodavanje piksela koji su "višak" nakon što visinu 
	 * redaka zaokružimo na cijeli broj.
	 * Metoda piksele raspoređuje uniformno.
	 * 
	 * @param extraWidthPixels broj piksela koji su višak
	 * @param row redak 
	 * @return 0 ako ne treba dodati piksel u taj redak, 1 ako treba
	 */
	private int addExtraHeightPixel(int extraWidthPixels, int row) {
		switch(extraWidthPixels) {
			case 0: 
				return 0;
			case 1:
				return row == 2 ? 1 : 0;
			case 2:
				return (row == 1 || row == 3) ? 1 : 0;
			case 3:
				return (row == 0 || row == 2 || row == 4) ? 1 : 0;
			case 4:
				return (row == 0 || row == 1 || row == 3 || row == 4) ? 1 : 0;
			default:
				return 0;
		}
	}

	/**
	 * Metoda za dodavanje piksela koji su "višak" nakon što širinu 
	 * stupaca zaokružimo na cijeli broj.
	 * Metoda piksele raspoređuje uniformno.
	 * 
	 * @param extraHeightPixels broj piksela koji su višak
	 * @param col stupac 
	 * @return 0 ako ne treba dodati piksel u taj stupac, 1 ako treba
	 */
	private int addExtraWidthPixel(int extraWidthPixels, int col) {
		switch(extraWidthPixels) {
		case 0: 
			return 0;
		case 1:
			return col == 3 ? 1 : 0;
		case 2:
			return (col == 2 || col == 4) ? 1 : 0;
		case 3:
			return (col == 0 || col == 3 || col == 5) ? 1 : 0;
		case 4:
			return (col == 0 || col == 2 || col == 4 || col == 6) ? 1 : 0;
		case 5:
			return (col == 0 || col == 1 || col == 3 || col == 5 || col == 6) ? 1 : 0;
		case 6:
			return (col == 0 || col == 1 || col == 2 || col == 4 || col == 5 || col == 6) ? 1 : 0;
		default:
			return 0;
		}
	}

	/**
	 * Metoda za dodavanje nove komponente u layout uz
	 * zadano ograničenje.
	 * Ovdje je dopušteno ograničenje tipa RCPosition ili String koji
	 * se može parsirati u RCPosition.
	 * 
	 * @param comp
	 * @param constraints
	 * @throws NullPointerException ako je predana null referenca
	 * 			za komponentu ili ograničnenje
	 * @throws CalcLayoutException ako već postoji komponenta sa
	 * 			zadanim ograničenjem ili ograničenje nije dobrog tipa.
	 */
	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		if(comp == null || constraints == null) {
			throw new NullPointerException("Komponenta i ograničenje ne mogu biti null referenca.");
		}
		
		// ako je String parsiraj
		if(constraints instanceof String) {
			constraints = parse(constraints.toString());
		} 
		
		// ako je RCPosition (ili parsirano iz Stringa u RCPosition) 
		if(constraints instanceof RCPosition) {
			validatePosition((RCPosition) constraints);
			
			int row = ((RCPosition) constraints).getRow() - 1;
			int column = ((RCPosition) constraints).getColumn() - 1;
			
			if(components[row][column] != null) {
				throw new CalcLayoutException("Već postoji komponenta s istim ograničenjem.");
			} 
			
			components[row][column] = comp;
			
		} else {
			throw new IllegalArgumentException("Ograničenje može biti tipa String ili RCPosition.");
		}		
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public void invalidateLayout(Container target) {		
	}

}
