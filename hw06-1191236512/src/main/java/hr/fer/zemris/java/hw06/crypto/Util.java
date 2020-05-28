package hr.fer.zemris.java.hw06.crypto;

/**
 * Razred koji nudi pomoćne metode za rad s nizom byte-ova.
 * 
 * @author Valentina Križ
 *
 */
public class Util {
	/**
	 * Metoda za pretvorbu stringa heksadekadskih brojeva 
	 * u niz odgovarajućih byteova.
	 * 
	 * @param keyText string znamenki
	 * @return niz byteova
	 * @throws IllegalArgumentException ako se u stringu nalazi nešto
	 * 			što nije heksadecimalni broj
	 */
	public static byte[] hextobyte(String keyText) {
		int len = keyText.length();
		
		if(len % 2 != 0) {
			throw new IllegalArgumentException("Expected even length text.");
		}
		byte[] bytearray = new byte[len / 2];
		
		for (int i = 0; i < len; i += 2) {
			try {
				bytearray[i / 2] = (byte) Integer.parseInt(keyText.substring(i, i + 2), 16);
			} catch(NumberFormatException ex) {
				throw new IllegalArgumentException("Invalid hex " + keyText.substring(i, i + 2) + ".");
			}
	    }
		
		return bytearray;
	}
	
	/**
	 * Metoda za pretvorbu niza byteova u string
	 * koji sadrži odgovarajuće heksadecimalne brojeve.
	 * 
	 * @param bytearray niz byteova
	 * @return string brojeva
	 */
	public static String bytetohex(byte[] bytearray) {
		StringBuilder str = new StringBuilder();
		
		for(int i = 0, len = bytearray.length; i < len; ++i) {
			str.append(String.format("%02x", bytearray[i]));
		}
		
		return str.toString();
	}
}
