package hr.fer.zemris.java.hw15.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Pomoćni razred za čuvanje i dohvaćanje
 * JPA EntityManagerFactory.
 * 
 * @author Valentina Križ
 *
 */
public class JPAEMFProvider {
	/**
	 * Primjerak JPA emf
	 * 
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * Getter za emf
	 * 
	 * @return emf
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Setter za emf
	 * 
	 * @param emf
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}