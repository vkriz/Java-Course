package hr.fer.zemris.java.gui.charts;

import java.awt.BorderLayout;
import java.awt.Container;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Program za demonstraciju korištenja razreda BarChart.
 * Program kao argument komandne linije prima
 * putanju do datoteke iz koje čita podatke za
 * crtanje stupčastog dijagrama.
 * Ukoliko datoteka ne postoji ili je neispravnog
 * oblika program ispisuje grešku i prestaje s radom.
 * 
 * Očekuje se datoteka oblika:
 * opis x-osi
 * opis y-osi
 * x1,y1 x2,y2 x3,y3 ...
 * minimalna vrijednost na y-osi
 * maksimalna vrijednost na y-osi
 * razmak između 2 susjedna y-a koji se prikazuju na osi
 * 
 * @author Valentina Križ
 *
 */
public class BarChartDemo extends JFrame {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Konstruktor koji prima tekst koji je potrebno
	 * ispisati i referencu na BarChart.
	 * 
	 * @param model
	 * @param text
	 */
	public BarChartDemo(BarChart model, String text) {
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Bar Chart v1.0");
		initGUI(model, text);
		setSize(800, 600);
		setLocationRelativeTo(null);
	}
	
	/**
	 * Metoda priprema prozor za prikaz korisniku.
	 * Postavlja labelu sa zadanim tekstom i 
	 * BarChart komponentu sa zadanom referencom na
	 * BarChart.
	 * 
	 * @param model
	 * @param text
	 */
	private void initGUI(BarChart model, String text) {
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		cp.add(new BarChartComponent(model), BorderLayout.CENTER);
		cp.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.PAGE_START);	
	}

	/**
	 * Metoda od koje počinje izvršavanje programa.
	 * 
	 * @param args argumenti komandne linije
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Program prima točno jedan parametar.");
			return;
		}
		try {
			Path filePath = Paths.get(args[0]);
			Scanner scanner = new Scanner(filePath.toFile());
			
			String xTitle = scanner.nextLine();
			String yTitle = scanner.nextLine();
			String[] valuesString = scanner.nextLine().split("\\s+");
			int minY = Integer.parseInt(scanner.nextLine());
			int maxY = Integer.parseInt(scanner.nextLine());
			int yGap = Integer.parseInt(scanner.nextLine());
			
			scanner.close();
		
			List<XYValue> values = new ArrayList<>();
			for(String value : valuesString) {
				String[] valueParts = value.split(",");
				if(valueParts.length != 2) {
					System.out.println("Neispravan oblik vrijednosti " + value + ".");
					return;
				}
				
				values.add(new XYValue(Integer.parseInt(valueParts[0]), Integer.parseInt(valueParts[1])));
			}
			
			BarChart model = new BarChart(values, xTitle, yTitle, minY, maxY, yGap);
			SwingUtilities.invokeLater(() -> {
				new BarChartDemo(model, filePath.toAbsolutePath().toString()).setVisible(true);
			});
		} catch (FileNotFoundException | NoSuchElementException | NumberFormatException ex) {
			System.out.println(ex.getMessage());
			return;
		}
	}
}
