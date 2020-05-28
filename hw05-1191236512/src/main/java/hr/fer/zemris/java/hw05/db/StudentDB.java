package hr.fer.zemris.java.hw05.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Razred predstavlja jednostavan DBMS.
 * Moguće je postavljanje upita oblika
 * query ime_atributa operator_uspoređivanja string
 * Dozvoljena imena atributa su jmbag, firstName i lastName.
 * Dozvoljeni operatori uspoređivanja su: <, <=, >, >=, !=, = i LIKE.
 * 
 * Za prestanak rada s bazom potrebno je upisati "exit".
 * 
 * @author Valentina Križ
 *
 */
public class StudentDB {
	/**
	 * Metoda koja dohvaća zapise o studentima iz baze koji zadovoljavaju upit.
	 * 
	 * @param db baza podataka
	 * @param line upit u obliku stringa
	 * @return lista zapisa studenata
	 */
	public static List<StudentRecord> getRecords(StudentDatabase db, String line) {
		List<StudentRecord> records = null;

		try {
			QueryParser parser = new QueryParser(line);
			if(parser.isDirectQuery()) {
				StudentRecord r = db.forJMBAG(parser.getQueriedJMBAG());
				System.out.println("Using index for record retrieval.");
				records = new ArrayList<>();
				if(r != null) {
					records.add(r);
				}
			} else {
				records = db.filter(new QueryFilter(parser.getQuery()));
			}
		} catch(QueryParserException ex) {
			System.out.println(ex.getMessage());
		} catch(IllegalArgumentException ex) {
			System.out.println(ex.getMessage());
		}
		return records;
	}
	
	/**
	 * Pomoćna metoda za ispis liste studenata.
	 * Metoda crta rub tablice zadanih dimenzija.
	 * 
	 * @param maxLastNameLength maksimalna duljina imena studenata
	 * @param maxFirstNameLength maksimalna duljina prezimena studenata
	 */
	private static void drawBorder(int maxLastNameLength, int maxFirstNameLength) {
		StringBuilder border = new StringBuilder();
		border.append('+');
		border.append("=".repeat(12));
		border.append('+');
		border.append("=".repeat(maxLastNameLength + 2));
		border.append('+');
		border.append("=".repeat(maxFirstNameLength + 2));
		border.append('+');
		border.append("=".repeat(3));
		border.append('+');
		
		System.out.println(border);
	}
	
	/**
	 * Pomoćna metoda za ispis jednog zapisa o studentu u tablicu.
	 * 
	 * @param record zapis o studentu
	 * @param maxLastNameLength maksimalna duljina imena studenata
	 * @param maxFirstNameLength maksimalna duljina prezimena studenata
	 */
	private static void printRecord(StudentRecord record, int maxLastNameLength, int maxFirstNameLength) {
		StringBuilder row = new StringBuilder();
		row.append("| ");
		row.append(record.getJmbag());
		row.append(" | ");
		row.append(record.getLastName());
		row.append(" ".repeat(maxLastNameLength - record.getLastName().length()));
		row.append(" | ");
		row.append(record.getFirstName());
		row.append(" ".repeat(maxFirstNameLength - record.getFirstName().length()));
		row.append(" | ");
		row.append(record.getFinalGrade());
		row.append(" |");
		System.out.println(row);
	}
	
	/**
	 * Metoda za ispis zapisa o studentima u obliku tablice.
	 * 
	 * @param records lista zapisa koje treba ispisati
	 */
	public static void printRecords(List<StudentRecord> records) {
		int maxFirstNameLength = 0;
		int maxLastNameLength = 0;
		
		for(StudentRecord record : records) {
			if(record.getFirstName().length() > maxFirstNameLength) {
				maxFirstNameLength = record.getFirstName().length();
			}

			if(record.getLastName().length() > maxLastNameLength) {
				maxLastNameLength = record.getLastName().length();
			}
		}
		
		if(records.size() > 0) {
			drawBorder(maxLastNameLength, maxFirstNameLength);
			for(StudentRecord record : records) {
				printRecord(record, maxLastNameLength, maxFirstNameLength);
			}
			drawBorder(maxLastNameLength, maxFirstNameLength);
		}
		System.out.println("Records selected: " + records.size());
	}
	
	/**
	 * Metoda od koje počinje izvođenje programa.
	 * 
	 * @param args argumenti komandne linije, ne koriste se
	 */
	public static void main(String[] args) {
		try {
			List<String> lines = Files.readAllLines(
					Paths.get("./database.txt"),
					StandardCharsets.UTF_8
			);
			try {
				StudentDatabase db = new StudentDatabase(lines);
				
				Scanner sc = new Scanner(System.in);
				while(true) {
					System.out.printf("> ");
					String command = sc.next();
					
					if(command.equals("exit")) {
						System.out.println("Goodbye!");
						break;
					} else if(command.equals("query")) {
						String line = sc.nextLine();
						List<StudentRecord> records = getRecords(db, line);
						if(records != null) {
							printRecords(records);
						}
					} else {
						System.out.println("Unsupported command!");
						sc.nextLine();
					}
				}
				sc.close();
			} catch(IllegalArgumentException ex) {
				// više studenata s istim jmbagom ili ocjena izvan raspona 1-5
				System.out.println(ex.getMessage());
				return;
			}
		} catch (IOException e1) {
			System.out.println("Greška pri čitanju datoteke.");
			return;
		}
	}
}
