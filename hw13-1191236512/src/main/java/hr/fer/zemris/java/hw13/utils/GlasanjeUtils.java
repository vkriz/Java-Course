package hr.fer.zemris.java.hw13.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pomoćni razred, služi za dohvaćanje
 * rezultata i bendova iz datoteka
 * glasanje-definicija.txt i glasanje-rezultati.txt.
 * 
 * @author Valentina Križ
 *
 */
public class GlasanjeUtils {
	/**
	 * Metoda dohvaća rezultate glasanja i 
	 * sprema ih u mapu s ključem id benda i 
	 * vrijednosti brojem glasova.
	 * 
	 * @param fileName
	 * @return mapa rezultata
	 */
	public static Map<String, Integer> getResults(String fileName) {
		Map<String, Integer> results = new HashMap<>();
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return results;
		}

		for(String line : lines) {
			String[] lineParts = line.split("\t");
			if(lineParts.length != 2) {
				break;
			}
			
			try {
				results.put(lineParts[0], Integer.parseInt(lineParts[1]));
			} catch(NumberFormatException ex) {
				break;
			}
		}
		return results; 
	}
	
	/**
	 * Metoda dohvaća popis svih bendova i 
	 * sprema ih u mapu s ključem id benda i 
	 * vrijednosti ime benda.
	 * 
	 * @param fileName
	 * @return mapa bendova
	 */
	public static Map<String, String> getBands(String fileName) {
		Map<String, String> results = new HashMap<>();
		List<String> lines;
		try {
			lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return results;
		}

		for(String line : lines) {
			String[] lineParts = line.split("\t");
			if(lineParts.length != 3) {
				break;
			}
			
			try {
				results.put(lineParts[0], lineParts[1]);
			} catch(NumberFormatException ex) {
				break;
			}
		}
		return results; 
	}
}
