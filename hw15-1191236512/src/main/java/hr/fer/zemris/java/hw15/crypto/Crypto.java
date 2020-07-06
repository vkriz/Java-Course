package hr.fer.zemris.java.hw15.crypto;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Razred služi za enkripciju lozinke prije
 * spremanja u bazu pomoću SHA-1 hasha.
 * 
 * @author Valentina Križ
 *
 */
public class Crypto {
	/**
	 * Metoda za kriptiranje lozinke pomoću
	 * SHA-1.
	 * 
	 * @param password originalna lozinka
	 * @return hashirana lozinka
	 * @throws NoSuchAlgorithmException
	 */
	public static String encryptPassword(String password) throws IOException, NoSuchAlgorithmException {
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		
		return bytetohex(sha.digest(password.getBytes()));
	}
	
	
	/**
	 * Metoda za pretvorbu niza byteova u string
	 * koji sadrži odgovarajuće heksadecimalne brojeve.
	 * 
	 * @param bytearray niz byteova
	 * @return string brojeva
	 */
	private static String bytetohex(byte[] bytearray) {
		StringBuilder str = new StringBuilder();
		
		for(int i = 0, len = bytearray.length; i < len; ++i) {
			str.append(String.format("%02x", bytearray[i]));
		}
		
		return str.toString();
	}
}
