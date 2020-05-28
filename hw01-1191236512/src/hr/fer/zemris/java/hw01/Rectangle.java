package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Razred Rectangle korisnicima omogućava računanje opsega i površine
 * pravokutnika sa zadanim veličinama stranica.
 * Unos stranica moguć je preko argumenata naredbenog retka ili preko konzole.
 * 
 * Ukoliko se koristi unos preko argumenata naredbenog retka potrebno je unesti točno 2 
 * argumenta - prvi je širina, a drugi dužina, a oba su cijeli ili decimalni brojevi.
 * 
 * @author Valentina Križ
 *
 */

public class Rectangle {
	
	/**
	 * Metoda služi za dohvaćanje visine ili širine iz konzole.
	 * 
	 * @param sc Scanner objekt
	 * @param side string s imenom stranice koju želimo dohvatiti
	 * @return unesena vrijednost koja se može protumačiti kao double
	 */
	
	private static double getSideLength(Scanner sc, String side) {
		double length = 0;
		
		while(true ) {
			System.out.printf("Unesite %s.%n", side);
			if(sc.hasNextLine()) {
				String input = sc.nextLine().trim();
				try {
					length = Double.parseDouble(input);
					if(length > 0) {
						break;
					} else {
						System.out.println("Unijeli ste negativnu vrijednost.");
					}
				} catch(NumberFormatException exc) {
					System.out.printf(
							"\'%s\' se ne može protumačiti kao broj.%n",
							input
					);
				}
			}
		}
		return length;
	}
	
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args argumenti zadani preko naredbenog retka
	 */
	public static void main(String[] args) {
		if(args.length != 2 && args.length != 0) {
			System.out.println("Program je potrebno pokrenuti s točno 0 ili 2 argumenata.");
			return;
		}
		
		double height = 0;
		double width = 0;
		
		if(args.length == 2) {
			for(int i = 0; i < 2; ++i) {
				try {
					if(i == 0) {
						width = Double.parseDouble(args[i]);
					} else {
						height = Double.parseDouble(args[i]);
					}
				} catch(NumberFormatException exc) {
					System.out.printf(
							"\'%s\' se ne može protumačiti kao broj.%n",
							args[i]);
					return;
				}
			}
			if(width <= 0 || height <= 0) {
				System.out.println("Unijeli ste negativnu vrijednost.");
				return;
			}
		} else if(args.length == 0) {
			Scanner sc = new Scanner(System.in);
			width = getSideLength(sc, "širinu");
			height = getSideLength(sc, "visinu");
			sc.close();
		}
		System.out.printf(
				"Pravokutnik širine %s i visine %s ima površinu %s te opseg %s.%n",
				Double.toString(width),
				Double.toString(height),
				Double.toString(width * height),
				Double.toString(2 * (width + height))
		);
	}
}
