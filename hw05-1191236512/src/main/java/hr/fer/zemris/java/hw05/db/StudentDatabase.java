package hr.fer.zemris.java.hw05.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Razred koji predstavlja bazu podataka studenata.
 * Sastoji se od liste zapisa o studentima te ima indeks
 * na atributu jmbag.
 * 
 * @author Valentina Križ
 *
 */
public class StudentDatabase {
	private List<StudentRecord> records;
	private Map<String, StudentRecord> index;
	
	/**
	 * Konstruktor koji prima listu redaka koji predstavljaju
	 * jedan zapis u bazi.
	 * 
	 * @param rows zapis
	 */
	public StudentDatabase(List<String> rows) {
		records = new ArrayList<>();
		index = new TreeMap<>();
		
		for(String row : rows) {
			String parts[] = row.trim().split("\\t");
			try {
				int grade = Integer.parseInt(parts[3]);
				if(grade < 1 || grade > 5) {
					throw new IllegalArgumentException("Ocjena mora biti broj između 1 i 5.");
				}
				StudentRecord rec = new StudentRecord(
										parts[0],
										parts[1],
										parts[2],
										grade);
				if(records.contains(rec)) {
					throw new IllegalArgumentException("Postoji više studenata s istim JMBAG-om.");
				}
				
				records.add(rec);
				index.put(parts[0], rec);
			} catch(NumberFormatException ex) {
				throw new IllegalArgumentException("Ocjena mora biti broj između 1 i 5.");
			}
		}
	}
	
	/**
	 * Metoda koja dohvaća zapis o studentu na temelju zadanog 
	 * jmbaga pomoću indeksa.
	 * 
	 * @param jmbag
	 * @return zapis o studentu
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return index.get(jmbag);
	}
	
	/**
	 * Metoda za filtriranje studenata.
	 * 
	 * @param filter
	 * @return zapisi studenata koji zadovoljavaju uvjete filtera
	 */
	public List<StudentRecord> filter(IFilter filter) {
		List<StudentRecord> accepted = new ArrayList<>();
		
		for(StudentRecord record : records) {
			if(filter.accepts(record)) {
				accepted.add(record);
			}
		}
		return accepted;
	}

}
