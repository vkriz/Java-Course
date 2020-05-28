package hr.fer.zemris.java.hw05.db.lexer;

/**
 * Razred koji prestavlja lekser za obavljanje
 * leksičke analize upita za bazu podataka.
 * 
 * @author Valentina Križ
 *
 */
public class QueryLexer {
	private char[] data;	// ulazni tekst
	private QueryToken token;		// trenutni token
	private int currentIndex;	// indeks prvog neobrađenog znaka
	
	/**
	 * Konstruktor koji prima ulazni tekst koji se tokenizira.
	 * 
	 * @param text ulazni tekst
	 */
	public QueryLexer(String text) {
		if(text == null) {
			throw new NullPointerException();
		}
		data = text.toCharArray();
		token = null;
		currentIndex = 0;
	}
	
	/**
	 * Pomoćna metoda koja pomiče currentIndex na sljedeći znak koji nije whitespace
	 * (ili na kraj data).
	 */
	private void moveToNextNonWhitespace() {
		while(currentIndex < data.length && Character.isWhitespace(data[currentIndex])) {
			++currentIndex;
		}
	}
	
	/**
	 * Metoda generira i vraća sljedeći token.
	 * 
	 * @return sljedeći token
	 * @throws QueryLexerException ukoliko dođe do pogreške
	 */
	public QueryToken nextToken() {
		// ako smo već pročitali zadnji znak
		if(token != null && token.getType() == QueryTokenType.EOL) {
			throw new QueryLexerException("Zadnji token je već pročitan.");
		}
		moveToNextNonWhitespace();
		
		if(currentIndex == data.length) {
			token = new QueryToken(QueryTokenType.EOL, null);
			return token;
		}
		
		if(data[currentIndex] == '=') {
			token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "EQUALS");
			++currentIndex;
			return token;
		}
		
		if(data[currentIndex] == '>') {
			if(currentIndex + 1 < data.length && data[currentIndex + 1] == '=') {
				currentIndex += 2;
				token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "GREATER_EQUALS");
				return token;
			} else {
				++currentIndex;
				token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "GREATER");
				return token;
			}
		}
		
		if(data[currentIndex] == '<') {
			if(currentIndex + 1 < data.length && data[currentIndex + 1] == '=') {
				currentIndex += 2;
				token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "LESS_EQUALS");
				return token;
			} else {
				++currentIndex;
				token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "LESS");
				return token;
			}
		}
		
		if(data[currentIndex] == '!') {
			if(currentIndex + 1 < data.length && data[currentIndex + 1] == '=') {
				currentIndex += 2;
				token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "NOT_EQUALS");
				return token;
			} else {
				throw new QueryLexerException("Nakon \"!\" mora slijediti \"=\".");
			}
		}
		
		if(data[currentIndex] == '\"') {
			++currentIndex;
			
			StringBuilder str = new StringBuilder();
			while(currentIndex < data.length && data[currentIndex] != '\"') {
				str.append(data[currentIndex]);
				++currentIndex;
			}
			
			if(currentIndex < data.length && data[currentIndex] == '\"') {
				++currentIndex;
				token = new QueryToken(QueryTokenType.STRING_LITERAL, str.toString());
				return token;
			} else {
				throw new QueryLexerException("String nije zatvoren.");
			}
		}
		
		// ako je ime atributa ili operator LIKE ili logički operator AND
		StringBuilder str = new StringBuilder();
		while(currentIndex < data.length && Character.isAlphabetic(data[currentIndex])) {
			str.append(data[currentIndex]);
			++currentIndex;
		}
		
		if(str.toString().equals("LIKE")) {
			token = new QueryToken(QueryTokenType.COMPARISON_OPERATOR, "LIKE");
			return token;
		} else if(str.toString().equalsIgnoreCase("AND")) {
			token = new QueryToken(QueryTokenType.AND, null);
			return token;
		} else {
			token = new QueryToken(QueryTokenType.ATTRIBUTE_NAME, str.toString());
			return token;
		}
	}
	
	/**
	 * Metoda vraća zadnji generirani token.
	 * Može se pozivati više puta; ne pokreće generiranje
	 * sljedećeg tokena.
	 * 
	 * @return zadnji generirani token
	 */
	public QueryToken getToken() {
		return token;
	}
}
