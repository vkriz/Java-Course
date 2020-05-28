package hr.fer.zemris.java.hw03.prob1;

public class Lexer {
	private char[] data;		// ulazni tekst
	private Token token;		// trenutni token
	private int currentIndex;	// indeks prvog neobrađenog znaka
	private LexerState state;	// stanje u kojemu se nalazi lekser
	
	/**
	 * Konstruktor koji prima ulazni tekst koji se tokenizira.
	 * 
	 * @param text ulazni tekst
	 */
	public Lexer(String text) {
		if(text == null) {
			throw new NullPointerException();
		}
		data = text.toCharArray();
		token = null;
		currentIndex = 0;
		setState(LexerState.BASIC);
	}
	
	/**
	 * Setter za state varijablu.
	 * 
	 * @param state nova vrijednost state varijable
	 * @throws NullPointerException ako je predana vrijednost null
	 */
	public void setState(LexerState state) {
		if(state == null) {
			throw new NullPointerException();
		}
		this.state = state;
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
	 * Pomoćna metoda koja dohvaća sljedeće neobrađeno slovo (ako ono slijedi) i dodaje ga u string.
	 * 
	 * @param word StringBuilder u koji se ubacuje slovo
	 * @return true ako je dohvaćeno sljedeće slovo, false inače
	 */
	private boolean getNextLetter(StringBuilder word) {
		if(currentIndex < data.length && Character.isLetter(data[currentIndex])) {
			word.append(data[currentIndex]);
			return true;
		}
		return false;
	}
	
	/**
	 * Pomoćna metoda koja dohvaća sljedeću escapeanu znamenku i dodaje ju u string.
	 * 
	 * @param word StringBuilder u koji se ubacuje znamenka
	 * @return true ako je dohvaćena sljedeća escapeana znamenka, false inače
	 */
	private boolean getNextEscapedDigit(StringBuilder word) {
		if(currentIndex < data.length && data[currentIndex] == '\\') {
			if(currentIndex + 1 == data.length || (!Character.isDigit(data[currentIndex+1]) && !(data[currentIndex+1] == '\\'))) {
				throw new LexerException();
			}
			
			++currentIndex;
			word.append(data[currentIndex]);
			return true;
		}
		return false;
	}
	
	/**
	 * Metoda generira i vraća sljedeći token.
	 * 
	 * @return sljedeći token
	 * @throws LexerException ukoliko dođe do pogreške
	 */
	public Token nextToken() {
		// ako smo već pročitali zadnji znak
		if(token != null && token.getType() == TokenType.EOF) {
			throw new LexerException();
		}
		
		moveToNextNonWhitespace();
		
		// ako čitamo zadnji znak
		if(currentIndex == data.length) {
			token = new Token(TokenType.EOF, null);
			return token;
		}
		
		// basic način rada
		if(state == LexerState.BASIC) {			
			// ako riječ
			if(Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
				StringBuilder word = new StringBuilder();
				while(getNextLetter(word) || getNextEscapedDigit(word)) {
					++currentIndex;
				}
				token = new Token(TokenType.WORD, word.toString());
				return token;
			}
			
			// ako je broj
			if(Character.isDigit(data[currentIndex])) {
				StringBuilder numberString = new StringBuilder();
				while(currentIndex < data.length && Character.isDigit(data[currentIndex])) {
					numberString.append(data[currentIndex]);
					++currentIndex;
				}
				try {
					Long number = Long.parseLong(numberString.toString());
					token = new Token(TokenType.NUMBER, number);
					return token;
				} catch (NumberFormatException ex) {
					throw new LexerException();
				}
			}
			
			// inače je simbol
			token = new Token(TokenType.SYMBOL, data[currentIndex]);
			if(data[currentIndex] == '#') {
				setState(LexerState.EXTENDED);
			}
			++currentIndex;
			return token;
			
		} else {
			// extended način rada
			
			// ako je riječ
			if(Character.isLetter(data[currentIndex]) 
					|| data[currentIndex] == '\\' 
					|| Character.isDigit(data[currentIndex])) {
				StringBuilder word = new StringBuilder();
				
				while(currentIndex < data.length 
						&& (Character.isLetter(data[currentIndex]) 
								|| data[currentIndex] == '\\' 
								|| Character.isDigit(data[currentIndex]))) {
					word.append(data[currentIndex]);
					++currentIndex;
				}
				
				token = new Token(TokenType.WORD, word.toString());
				return token;
			}
			
			// inače je simbol
			token = new Token(TokenType.SYMBOL, data[currentIndex]);
			if(data[currentIndex] == '#') {
				setState(LexerState.BASIC);
			}
			++currentIndex;
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
	public Token getToken() {
		return token;
	}
}
