package hr.fer.zemris.java.gui.layouts.demo;

import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

/**
 * Razred služi za demonstraciju rada upravljača razmještaja
 * CalcLayout. Program stvara novi prozor u koji dodaje labele
 * na odabrane položaje.
 * 
 * @author Valentina Križ
 *
 */
public class DemoFrame1 extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default konstruktor, stvara novi prozor
	 * i puni ga sadržajem.
	 */
	public DemoFrame1() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		initGUI();
		pack();
	}

	/**
	 * Metoda stvara novi container s upravljačem
	 * razmještaja CalcLayout i dodaje nekoliko labela.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new CalcLayout(3));
		cp.add(l("tekst 1"), new RCPosition(1,1));
		cp.add(l("tekst 2"), new RCPosition(2,3));
		cp.add(l("tekst stvarno najdulji"), new RCPosition(2,7));
		cp.add(l("tekst kraći"), new RCPosition(4,2));
		cp.add(l("tekst srednji"), new RCPosition(4,5));
		cp.add(l("tekst"), new RCPosition(4,7));
	}

	/**
	 * Metoda stvara labelu sa zadanim tekstom
	 * i žutom bojom pozadine.
	 * 
	 * @param text
	 * @return stvorena labela
	 */
	private JLabel l(String text) {
		JLabel l = new JLabel(text);
		l.setBackground(Color.YELLOW);
		l.setOpaque(true);
		return l;
	}
	
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DemoFrame1().setVisible(true);
		});
	}	
}
