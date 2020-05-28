package hr.fer.zemris.java.gui.prim;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program po čijem se pokretanju otvara prozor
 * u koje se prikazuju dvije liste prostih brojeva (jednako visoke,
 * jednako široke, rastegnute preko čiatve površine prozora
 * izuzev donjeg ruba). Uz donji rub se nalazi gumb "sljedeći".
 * Klikom na gumb na obje liste se dodaje sljedeći prosti broj.
 * 
 * @author Valentina Križ
 *
 */
public class PrimDemo extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Default konstruktor.
	 */
	public PrimDemo() {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(500, 500);
		setTitle("Prim numbers v1.0");
		initGUI();
	}

	/**
	 * Metoda za inicijalizaciju prozora.
	 * U prozor dodaje komponente za prikaz listi
	 * i gumba.
	 */
	private void initGUI() {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		
		PrimListModel model = new PrimListModel();
		
		JPanel panel = new JPanel(new GridLayout(0,2));
		JList<Integer> list1 = new JList<>(model);
		JList<Integer> list2 = new JList<>(model);
		
		list1.setBorder(BorderFactory.createLoweredBevelBorder());
		list2.setBorder(BorderFactory.createLoweredBevelBorder());
	
		panel.add(new JScrollPane(list1));
		panel.add(new JScrollPane(list2));
		
		JButton next = new JButton("Sljedeći");
		next.addActionListener(e -> model.next());
		
		cp.add(next, BorderLayout.PAGE_END);
		cp.add(panel, BorderLayout.CENTER);
	}

	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new PrimDemo().setVisible(true);
		});
	}
}
