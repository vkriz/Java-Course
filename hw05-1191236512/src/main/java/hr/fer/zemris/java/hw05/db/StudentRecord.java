package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji predstavlja zapis o jednom studentu u bazi podataka.
 * Zapis sadrži jmbag, ime, prezime i ocjenu.
 * 
 * @author Valentina Križ
 *
 */
public class StudentRecord {
	private String jmbag;
	private String lastName;
	private String firstName;
	private int finalGrade;
	
	/**
	 * Konstruktor koji prima vrijednosti svih varijabli u zapisu.
	 * 
	 * @param jmbag
	 * @param lastName
	 * @param firstName
	 * @param finalGrade
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		this.jmbag = jmbag;
		this.lastName = lastName;
		this.firstName = firstName;
		this.finalGrade = finalGrade;
	}
	
	/**
	 * Getter za varijablu jmbag.
	 * 
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}
	
	/**
	 * Getter za varijablu lastName
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Getter za varijablu firstName
	 * 
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Getter za varijablu finalGrade
	 * 
	 * @return finalGrade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	/**
	 * Metoda koja vraća hashCode zapisa, temelji se na jmbagu
	 * studenta.
	 * 
	 * @return hashCode
	 */
	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	/**
	 * Metoda koja uspoređuje dva zapisa studenta na 
	 * temelju jmbaga.
	 * U bazi nisu dozvoljena dva zapisa s istim JMBAG-om.
	 * 
	 * @param obj zapis s kojim uspoređujemo
	 * @return true ako su zapisi jednaki (imaju isti JMBAG),
	 * 			false inače
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof StudentRecord)) {
			return false;
		}
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
}
