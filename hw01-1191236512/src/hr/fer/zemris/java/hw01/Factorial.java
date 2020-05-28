package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 * Razred Factorial korisnicima omogućava unos brojeva i računanje njihovih faktorijela.
 * 
 * @author Valentina Križ
 *
 */

public class Factorial {
	
	/**
	 * Metoda računa faktorijelu broja koji je predan kao argument ako faktorijela tog broja postoji.
	 * Metoda ne može računati faktorijelu broja većeg od 20 jer ona nije prikaziva u broju bitova
	 * koje metoda koristi.
	 *
	 * @param number broj čiju faktorijelu treba izračunati
	 * @return vrijednost faktorijele zadanog broja ako ona postoji i prikaziva je
	 * 
	 * @throws IllegalArgumentException ako se kao argument preda broj manji od 0 ili veći od 20
	 */
	
	private static long calculateFactorial(int number) {
		if(number < 0 || number > 20) {
			throw new IllegalArgumentException(
					"Predani broj nije unutar dozvoljenog raspona."
					);
		}
		
		long res = 1;
		
		for(int i = 2; i <= number; ++i) {
			res = res * i;
		}
		
		return res;
	}
	
	/**
	 * Metoda od koje kreće izvođenje programa.
	 * 
	 * @param args argumenti zadani preko naredbenog retka
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		while(true) {
			System.out.println("Unesite broj");
			
			if(sc.hasNextLine()) {
				String line = sc.nextLine().trim();
				if(line.equals("kraj")) {
					System.out.println("Doviđenja.");
					break;
				}
				
				try {
					int number =  Integer.parseInt(line.trim());
					if(number < 3 || number > 20) {
						System.out.printf(
								"\'%d\' nije broj u dozvoljenom rasponu.%n",
								number
						);
					} else {
						System.out.printf(
								"%d! = %d%n",
								number,
								calculateFactorial(number)
						);
					}
				} catch(NumberFormatException exc) {
					System.out.printf(
							"\'%s\' nije cijeli broj.%n", 
							line
					);
				}
			}
		}
		sc.close();
	}
}
