package hr.fer.zemris.java.hw06.shell;

/**
 * Razred koji nudi pomoćne metode za rad sa stringovima
 * koji predstavljaju argumente MyShell narebdi.
 * 
 * @author Valentina Križ
 *
 */
public class Util {
	/**
	 * Pomoćna metoda za pomicanje na sljedeći non-whitespace znak u stringu.
	 * 
	 * @param arguments 
	 * @param currentIndex
	 * @return index sljedećeg non-whitespace znaka
	 */
	private static int goToNextNonWhitespace(String arguments, int currentIndex) {
		while(currentIndex < arguments.length() && Character.isWhitespace(arguments.charAt(currentIndex))) {
			++currentIndex;
		}
		
		return currentIndex;
	}
	
	/**
	 * Pomoćna za pomicanje na sljedeći whitespace znak u stringu.
	 * 
	 * @param arguments
	 * @param currentIndex
	 * @return index sljedećeg whitespace znaka
	 */
	private static int goToNextWhitespace(String arguments, int currentIndex) {
		while(currentIndex < arguments.length() && !Character.isWhitespace(arguments.charAt(currentIndex))) {
			++currentIndex;
		}
		
		return currentIndex;
	}
	
	/**
	 * Pomoćna metoda za dohvaćanje imena datoteke unutar navodnika.
	 * 
	 * @param arguments
	 * @param currentIndex
	 * @return ime datoteke (s navodnicima)
	 */
	private static String getQuotedFilename(String arguments, int currentIndex) {
		StringBuilder sb = new StringBuilder();
		sb.append('\"');
		++currentIndex;
		
		boolean flag = false;
		while(currentIndex < arguments.length()) {
			// ako je " bez escape-a, onda je closing quote
			if(arguments.charAt(currentIndex) == '\"' && !flag) {
				break;
			}
			
			if(arguments.charAt(currentIndex) == '\\') {
				flag = true;
				++currentIndex;
				continue;
			}
			
			if(flag) {
				// ako je prethodni znak bio \, a trenutni nije niti " niti \, dodaj \ u string (jer nije escape znaka)
				if(arguments.charAt(currentIndex) != '\"' && arguments.charAt(currentIndex) != '\"') {
					sb.append('\\');
				}
				flag = false;
			}
			
			// inače će se dodati samo " ili \
			sb.append(arguments.charAt(currentIndex));
			++currentIndex;
		}
		
		if(currentIndex == arguments.length()) {
			throw new IllegalArgumentException("Closing qouote missing.");
		}
		
		sb.append('\"');
		return sb.toString();
	}
	
	/**
	 * Pomoćna metoda za micanje navodnika iz imena datoteke (početnog i krajnjeg).
	 * 
	 * @param fileName
	 * @return ime datoteke bez navodnika
	 */
	public static String removeQuotes(String fileName) {
		if(fileName.charAt(0) == '\"' && fileName.charAt(fileName.length() - 1) == '\"') {
			fileName = fileName.substring(1, fileName.length() - 1);
		}
		return fileName;
	}
	
	/**
	 * Pomoćna metoda koja vraća sljedeći argument iz stringa (prvi od predanog indeksa nadalje).
	 * 
	 * @param arguments
	 * @param isFilename boolean varijabla koja govori može li argument počinjati navodnikom
	 * @param currentIndex trenutni indeks
	 * @return sljedeći argument
	 * @throws IllegalArgumentException ako argument počinje s navodnicima, a ne očekuje se ime datoteke
	 */
	public static String getNextArgument(String arguments, boolean isFilename, int currentIndex) {
		currentIndex = goToNextNonWhitespace(arguments, currentIndex);

		if(!isFilename && arguments.charAt(currentIndex) == '\"') {
			throw new IllegalArgumentException();
		}
		
		if(!isFilename || arguments.charAt(currentIndex) != '\"') {
			int argumentEnding = goToNextWhitespace(arguments, currentIndex);
			return arguments.substring(currentIndex, argumentEnding);
		}

		return getQuotedFilename(arguments, currentIndex);
	}
}
